package com.invoiceapp.findoc;

import com.invoiceapp.branch.BranchRepository;
import com.invoiceapp.customer.Customer;
import com.invoiceapp.customer.CustomerRepository;
import com.invoiceapp.documenttype.DocumentTypeRepository;
import com.invoiceapp.findoc.dto.FindocCreateRequest;
import com.invoiceapp.findoc.dto.FindocResponse;
import com.invoiceapp.findoc.dto.MtrLineRequest;
import com.invoiceapp.global.DocumentDomain;
import com.invoiceapp.global.DocumentStatus;
import com.invoiceapp.mtrl.Mtrl;
import com.invoiceapp.mtrl.MtrlRepository;
import com.invoiceapp.series.Series;
import com.invoiceapp.series.SeriesRepository;
import com.invoiceapp.seriescounter.SeriesYearCounterService;
import com.invoiceapp.seriescounter.dto.NextNumberRequest;
import com.invoiceapp.seriescounter.dto.NextNumberResponse;
import com.invoiceapp.vat.Vat;
import com.invoiceapp.vat.VatRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class FindocServiceImpl implements FindocService {

    private final FindocRepository findocRepo;
    private final MtrlinesRepository lineRepo;
    private final DocumentTypeRepository docTypeRepo;
    private final BranchRepository branchRepo;
    private final SeriesRepository seriesRepo;
    private final CustomerRepository customerRepo;
    private final MtrlRepository mtrlRepo;
    private final VatRepository vatRepo;
    private final SeriesYearCounterService counterSvc;

    public FindocServiceImpl(FindocRepository findocRepo,
                             MtrlinesRepository lineRepo,
                             DocumentTypeRepository docTypeRepo,
                             BranchRepository branchRepo,
                             SeriesRepository seriesRepo,
                             CustomerRepository customerRepo,
                             MtrlRepository mtrlRepo,
                             VatRepository vatRepo,
                             SeriesYearCounterService counterSvc) {
        this.findocRepo = findocRepo;
        this.lineRepo = lineRepo;
        this.docTypeRepo = docTypeRepo;
        this.branchRepo = branchRepo;
        this.seriesRepo = seriesRepo;
        this.customerRepo = customerRepo;
        this.mtrlRepo = mtrlRepo;
        this.vatRepo = vatRepo;
        this.counterSvc = counterSvc;
    }

    @Override
    public FindocResponse createDraft(FindocCreateRequest req) {
        if (req.documentTypeId() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "documentTypeId is required");
        if (req.customerId() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "customerId is required");
        if (req.documentDomain() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "documentDomain is required");
        LocalDate docDate = (req.documentDate() != null) ? req.documentDate() : LocalDate.now();

        var dt = docTypeRepo.findById(req.documentTypeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "DocumentType not found: " + req.documentTypeId()));

        Series series = null;
        if (req.seriesId() != null) {
            series = seriesRepo.findById(req.seriesId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Series not found: " + req.seriesId()));
        }

        var branch = (req.branchId() != null) ? branchRepo.findById(req.branchId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Branch not found: " + req.branchId()))
                : null;

        Customer customer = customerRepo.findById(req.customerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer not found: " + req.customerId()));

        Findoc f = new Findoc();
        f.setDocumentType(dt);
        f.setSeries(series);
        f.setBranch(branch);
        f.setCustomer(customer);
        f.setDocumentDate(docDate);
        f.setDocumentDomain(DocumentDomain.fromCode(req.documentDomain()));
        f.setStatus(DocumentStatus.DRAFT);
        return toDto(findocRepo.save(f));
    }

    @Override
    public FindocResponse upsertLine(Long findocId, MtrLineRequest lineReq) {
        Findoc f = mustBeEditable(findocId);

        Mtrl mtrl = mtrlRepo.findById(lineReq.mtrlId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mtrl not found: " + lineReq.mtrlId()));
        Vat vat = vatRepo.findById(lineReq.vatId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vat not found: " + lineReq.vatId()));

        Mtrlines line = f.getLines().stream()
                .filter(l -> l.getLineNo().equals(lineReq.lineNo()))
                .findFirst()
                .orElseGet(() -> {
                    Mtrlines nl = new Mtrlines();
                    nl.setFindoc(f);
                    nl.setLineNo(lineReq.lineNo());
                    f.addLine(nl);
                    return nl;
                });

        line.setMtrl(mtrl);
        line.setVat(vat);
        line.setQty(nz(lineReq.qty()));
        line.setPrice(nz(lineReq.price()));
        line.setDiscountRate(nz(lineReq.discountRate()));

        recalcLine(line);
        recalcTotals(f);

        return toDto(f);
    }

    @Override
    public FindocResponse deleteLine(Long findocId, Long lineId) {
        Findoc f = mustBeEditable(findocId);
        Mtrlines l = lineRepo.findById(lineId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Line not found: id=" + lineId));
        if (!l.getFindoc().getId().equals(f.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Line does not belong to this document");
        }
        f.getLines().removeIf(x -> x.getId().equals(lineId));
        lineRepo.delete(l);

        recalcTotals(f);
        return toDto(f);
    }

    @Override
    public FindocResponse post(Long findocId) {
        Findoc f = findocRepo.findById(findocId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Findoc not found: " + findocId));

        if (f.getStatus() != DocumentStatus.DRAFT)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only DRAFT documents can be posted");

        if (f.getSeries() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Series is required to post document");

        if (f.getLines().isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No lines to post");

        // re-calc totals
        f.getLines().forEach(this::recalcLine);
        recalcTotals(f);

        // numbering
        NextNumberResponse nn = counterSvc.nextNumber(
                new NextNumberRequest(f.getSeries().getId(), f.getDocumentDate())
        );
        f.setFiscalYear(nn.fiscalYear());
        f.setNumber(nn.number());
        f.setPrintedNumber(nn.formatted());

        f.setStatus(DocumentStatus.POSTED);

        return toDto(f);
    }

    @Override
    public FindocResponse cancel(Long findocId) {
        Findoc f = findocRepo.findById(findocId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Findoc not found: " + findocId));

        if (f.getStatus() != DocumentStatus.POSTED)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only POSTED documents can be cancelled");

        f.setStatus(DocumentStatus.CANCELLED);
        return toDto(f);
    }

    @Override @Transactional(readOnly = true)
    public FindocResponse get(Long id) {
        return findocRepo.findById(id).map(this::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Findoc not found: " + id));
    }

    @Override @Transactional(readOnly = true)
    public Page<FindocResponse> list(Pageable pageable, DocumentStatus status) {
        return (status == null ? findocRepo.findAll(pageable) : findocRepo.findAllByStatus(status, pageable))
                .map(this::toDto);
    }

    private Findoc mustBeEditable(Long findocId) {
        Findoc f = findocRepo.findById(findocId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Findoc not found: " + findocId));
        if (f.getStatus() != DocumentStatus.DRAFT)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Document is not editable");
        return f;
    }

    private void recalcLine(Mtrlines l) {
        var qty = nz(l.getQty());
        var price = nz(l.getPrice());
        var disc = nz(l.getDiscountRate());
        var vatRate = l.getVat() != null ? nz(l.getVat().getRate()) : BigDecimal.ZERO;

        BigDecimal gross = price.multiply(qty);
        BigDecimal net = gross.multiply(BigDecimal.ONE.subtract(disc));
        BigDecimal vatAmount = net.multiply(vatRate);
        BigDecimal total = net.add(vatAmount);

        l.setNetAmount(scale(net));
        l.setVatAmount(scale(vatAmount));
        l.setTotalAmount(scale(total));
    }

    private void recalcTotals(Findoc f) {
        BigDecimal net = BigDecimal.ZERO;
        BigDecimal vat = BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;

        for (Mtrlines l : f.getLines()) {
            net   = net.add(nz(l.getNetAmount()));
            vat   = vat.add(nz(l.getVatAmount()));
            total = total.add(nz(l.getTotalAmount()));
        }
        f.setTotalNet(scale(net));
        f.setTotalVat(scale(vat));
        f.setTotalGross(scale(total));

        f.getLines().sort(Comparator.comparing(Mtrlines::getLineNo));
        for (int i = 0; i < f.getLines().size(); i++) {
            if (f.getLines().get(i).getLineNo() == null)
                f.getLines().get(i).setLineNo(i + 1);
        }
    }

    private static java.math.BigDecimal nz(java.math.BigDecimal x) { return x == null ? java.math.BigDecimal.ZERO : x; }
    private static java.math.BigDecimal scale(java.math.BigDecimal x) { return x.setScale(2, java.math.RoundingMode.HALF_UP); }

    private FindocResponse toDto(Findoc e) {
        List<FindocResponse.FindocLineResponse> lines = e.getLines().stream()
                .sorted(Comparator.comparing(Mtrlines::getLineNo))
                .map(l -> new FindocResponse.FindocLineResponse(
                        l.getId(),
                        l.getLineNo(),
                        l.getMtrl() != null ? l.getMtrl().getId() : null,
                        l.getMtrl() != null ? safe(l.getMtrl().getCode()) : null,
                        l.getMtrl() != null ? safe(l.getMtrl().getName()) : null,
                        l.getVat() != null ? l.getVat().getId() : null,
                        l.getVat() != null ? l.getVat().getRate() : null,
                        l.getQty(),
                        l.getPrice(),
                        l.getDiscountRate(),
                        l.getNetAmount(),
                        l.getVatAmount(),
                        l.getTotalAmount()
                )).toList();

        return new FindocResponse(
                e.getId(),
                e.getCompanyId(),
                e.getDocumentType() != null ? e.getDocumentType().getId() : null,
                e.getBranch() != null ? e.getBranch().getId() : null,
                e.getSeries() != null ? e.getSeries().getId() : null,
                e.getDocumentDomain().getCode(),
                e.getFiscalYear(),
                e.getNumber(),
                e.getPrintedNumber(),
                e.getDocumentDate(),
                e.getCustomer() != null ? e.getCustomer().getId() : null,
                e.getStatus().getCode(),
                e.getTotalNet(),
                e.getTotalVat(),
                e.getTotalGross(),
                lines
        );
    }

    private static String safe(String s) { return (s == null || s.isBlank()) ? null : s; }
}
