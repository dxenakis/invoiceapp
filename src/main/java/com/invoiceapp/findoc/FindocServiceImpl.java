package com.invoiceapp.findoc;

import com.invoiceapp.branch.Branch;
import com.invoiceapp.branch.BranchRepository;
import com.invoiceapp.documenttype.DocumentType;
import com.invoiceapp.documenttype.DocumentTypeRepository;
import com.invoiceapp.findoc.dto.FindocCreateRequest;
import com.invoiceapp.findoc.dto.FindocResponse;
import com.invoiceapp.findoc.mtrlines.dto.MtrLineRequest;
import com.invoiceapp.findoc.enums.DocumentDomain;
import com.invoiceapp.findoc.enums.DocumentStatus;
import com.invoiceapp.findoc.mtrlines.Mtrlines;
import com.invoiceapp.findoc.mtrlines.dto.MtrLineResponse;
import com.invoiceapp.findoc.mtrlines.MtrlinesRepository;
import com.invoiceapp.global.enums.TraderDomain;
import com.invoiceapp.mtrl.Mtrl;
import com.invoiceapp.mtrl.MtrlRepository;
import com.invoiceapp.series.Series;
import com.invoiceapp.series.SeriesRepository;
import com.invoiceapp.seriescounter.SeriesYearCounterService;
import com.invoiceapp.seriescounter.dto.NextNumberRequest;
import com.invoiceapp.trader.Trader;
import com.invoiceapp.trader.TraderRepository;
import com.invoiceapp.vat.Vat;
import com.invoiceapp.vat.VatRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.invoiceapp.payment.PaymentMethod;
import com.invoiceapp.payment.PaymentMethodRepository;
import com.invoiceapp.shipkind.ShipKind;
import com.invoiceapp.shipkind.ShipKindRepository;
import com.invoiceapp.mtrunit.MtrUnit;
import com.invoiceapp.mtrunit.MtrUnitRepository;
import com.invoiceapp.whouse.Whouse;
import com.invoiceapp.whouse.WhouseRepository;
import com.invoiceapp.findoc.mtrdoc.Mtrdoc;
import com.invoiceapp.findoc.mtrdoc.MtrdocRepository;
import com.invoiceapp.findoc.mtrdoc.dto.MtrdocRequest;
import com.invoiceapp.findoc.mtrdoc.dto.MtrdocResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
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
    private final TraderRepository traderRepo;
    private final MtrlRepository mtrlRepo;
    private final VatRepository vatRepo;
    private final SeriesYearCounterService counterSvc;
    private final PaymentMethodRepository paymentRepo;
    private final ShipKindRepository shipKindRepo;
    private final MtrUnitRepository mtrUnitRepo;
    private final WhouseRepository whouseRepo;
    private final MtrdocRepository mtrdocRepo;
    public FindocServiceImpl(FindocRepository findocRepo,
                             MtrlinesRepository lineRepo,
                             DocumentTypeRepository docTypeRepo,
                             BranchRepository branchRepo,
                             SeriesRepository seriesRepo,
                             TraderRepository traderRepo,
                             MtrlRepository mtrlRepo,
                             VatRepository vatRepo,
                             PaymentMethodRepository paymentRepo,
                             ShipKindRepository shipKindRepo,
                             MtrUnitRepository mtrUnitRepo,
                             WhouseRepository whouseRepo,
                             MtrdocRepository mtrdocRepo,
                             SeriesYearCounterService counterSvc) {
        this.findocRepo = findocRepo;
        this.lineRepo = lineRepo;
        this.docTypeRepo = docTypeRepo;
        this.branchRepo = branchRepo;
        this.seriesRepo = seriesRepo;
        this.traderRepo = traderRepo;
        this.mtrlRepo = mtrlRepo;
        this.vatRepo = vatRepo;
        this.paymentRepo = paymentRepo;
        this.shipKindRepo = shipKindRepo;
        this.mtrUnitRepo = mtrUnitRepo;
        this.whouseRepo = whouseRepo;
        this.mtrdocRepo = mtrdocRepo;
        this.counterSvc = counterSvc;
    }

    // │────────────────────────────────────────│
    // │  Βοηθητικές μέθοδοι φόρτωσης entities   │
    // │────────────────────────────────────────│

    private Findoc getFindocOr404(Long id) {
        return findocRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Findoc not found: " + id));
    }

    private DocumentType getDocumentType(Long id) {
        return docTypeRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "DocumentType not found: " + id));
    }

    private Branch getBranch(Long id) {
        return branchRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Branch not found: " + id));
    }

    private Series getSeries(Long id) {
        return seriesRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Series not found: " + id));
    }

    private Trader getTrader(Long id) {
        return traderRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trader not found: " + id));
    }

    private Mtrl getMtrl(Long id) {
        return mtrlRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mtrl not found: " + id));
    }

    private Vat getVat(Long id) {
        return vatRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vat not found: " + id));
    }

    private PaymentMethod getPayment(Long id) {
        return paymentRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "PaymentMethod not found: " + id));
    }

    private ShipKind getShipKind(Long id) {
        return shipKindRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "ShipKind not found: " + id));
    }

    private MtrUnit getMtrUnit(Long id) {
        return mtrUnitRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "MtrUnit not found: " + id));
    }

    private Whouse getWhouse(Long id) {
        return whouseRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Whouse not found: " + id));
    }

    // │────────────────────────────────────────│
    // │   TraderDomain vs DocumentDomain       │
    // │────────────────────────────────────────│

    private void validateTraderForDocument(DocumentDomain docDomain, Trader trader) {
        TraderDomain role = trader.getTraderDomain();

        boolean ok = switch (docDomain) {
            case SALES, COLLECTIONS -> role == TraderDomain.CUSTOMER;
            case PURCHASES, PAYMENTS -> role == TraderDomain.SUPPLIER;
        };

        if (!ok) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Trader " + trader.getId() +
                            " (" + trader.getTraderDomain() + ")" +
                            " δεν επιτρέπεται για έγγραφο domain=" + docDomain
            );
        }
    }

    // │────────────────────────────────────────│
    // │              createDraft               │
    // │────────────────────────────────────────│

    @Override
    public FindocResponse createDraft(FindocCreateRequest req) {

        if (req.documentTypeId() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "documentTypeId required");
        if (req.branchId() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "branchId required");
        if (req.seriesId() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "seriesId required");
        if (req.traderId() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "traderId required");
        if (req.documentDate() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "documentDate required");
        if (req.documentDomain() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "documentDomain required");

        DocumentDomain domain = DocumentDomain.fromCode(req.documentDomain());
        DocumentType dt = getDocumentType(req.documentTypeId());
        Branch br = getBranch(req.branchId());
        Series sr = getSeries(req.seriesId());
        Trader trader = getTrader(req.traderId());

        PaymentMethod payment = null;
        if (req.paymentMethodId() != null) {
            payment = getPayment(req.paymentMethodId());
        }

        ShipKind shipKind = null;
        if (req.shipKindId() != null) {
            shipKind = getShipKind(req.shipKindId());
        }
        validateTraderForDocument(domain, trader);

        LocalDate docDate = req.documentDate();
        int fiscalYear = docDate.getYear();

        Findoc f = new Findoc();
        f.setDocumentType(dt);
        f.setBranch(br);
        f.setSeries(sr);
        f.setTrader(trader);
        f.setDocumentDomain(domain);
        f.setDocumentDate(docDate);
        f.setFiscalYear(fiscalYear);
        f.setStatus(DocumentStatus.DRAFT);
        f.setTotalNet(BigDecimal.ZERO);
        f.setTotalVat(BigDecimal.ZERO);
        f.setTotalGross(BigDecimal.ZERO);
        f.setPaymentMethod(payment);
        f.setShipKind(shipKind);
        return toResponse(findocRepo.save(f));
    }

    // │────────────────────────────────────────│
    // │               upsertLine               │
    // │────────────────────────────────────────│

    @Override
    public FindocResponse upsertLine(Long findocId, MtrLineRequest req) {

        Findoc f = getFindocOr404(findocId);

        if (f.getStatus() != DocumentStatus.DRAFT)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only draft documents editable");

        Mtrl m = getMtrl(req.mtrlId());

        // VAT: από request ή από είδος
        Vat v;
        if (req.vatId() != null) {
            v = getVat(req.vatId());
        } else if (m.getVat() != null) {
            v = m.getVat();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vat is required for line");
        }

        // MtrUnit: από request ή από είδος
        MtrUnit u = null;
        if (req.mtrUnitId() != null) {
            u = getMtrUnit(req.mtrUnitId());
        } else if (m.getMtrUnit() != null) {
            u = m.getMtrUnit();
        }

        // Whouse: από request ή από mtrdoc του παραστατικού
        Whouse wh = null;
        if (req.whouseId() != null) {
            wh = getWhouse(req.whouseId());
        } else if (f.getMtrdoc() != null && f.getMtrdoc().getWhouse() != null) {
            wh = f.getMtrdoc().getWhouse();
        }

        BigDecimal qty = nz(req.qty());
        BigDecimal price = nz(req.price());
        BigDecimal dr = nz(req.discountRate());

        BigDecimal lineGross = qty.multiply(price);
        BigDecimal discount = lineGross.multiply(dr).divide(new BigDecimal("100"));
        BigDecimal net = lineGross.subtract(discount);
        BigDecimal vatAmount = net.multiply(v.getRate()).divide(new BigDecimal("100"));
        BigDecimal total = net.add(vatAmount);

        // find or create line
        List<Mtrlines> lines = lineRepo.findByFindocIdOrderByLineNoAsc(findocId);

        Mtrlines line = lines.stream()
                .filter(l -> l.getLineNo().equals(req.lineNo()))
                .findFirst()
                .orElseGet(() -> {
                    Mtrlines nl = new Mtrlines();
                    nl.setFindoc(f);
                    return nl;
                });

        line.setLineNo(req.lineNo());
        line.setMtrl(m);
        line.setVat(v);
        line.setMtrUnit(u);
        line.setWhouse(wh);
        line.setQty(qty);
        line.setPrice(price);
        line.setDiscountRate(dr);
        line.setNetAmount(net);
        line.setVatAmount(vatAmount);
        line.setTotalAmount(total);

        lineRepo.save(line);
        recalcTotals(f);

        return toResponse(findocRepo.save(f));
    }

    // │────────────────────────────────────────│
    // │               deleteLine               │
    // │────────────────────────────────────────│

    @Override
    public FindocResponse deleteLine(Long findocId, Long lineId) {

        Findoc f = getFindocOr404(findocId);

        if (f.getStatus() != DocumentStatus.DRAFT)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only draft documents editable");

        try {
            lineRepo.deleteById(lineId);
        } catch (EmptyResultDataAccessException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Line not found: " + lineId);
        }

        recalcTotals(f);
        return toResponse(findocRepo.save(f));
    }

    // │────────────────────────────────────────│
    // │       post (οριστικοποίηση)            │
    // │────────────────────────────────────────│

    @Override
    public FindocResponse post(Long id) {

        Findoc f = getFindocOr404(id);

        if (f.getStatus() != DocumentStatus.DRAFT)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only draft can be posted");

        List<Mtrlines> lines = lineRepo.findByFindocIdOrderByLineNoAsc(id);
        if (lines.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot post empty document");

        var counter = counterSvc.nextNumber(new NextNumberRequest(f.getSeries().getId() , f.getDocumentDate()  ));

        f.setNumber(counter.number());
        f.setPrintedNumber(String.valueOf(counter.number()));
        //counter.printedNumber()
        recalcTotals(f);
        f.setStatus(DocumentStatus.POSTED);

        return toResponse(findocRepo.save(f));
    }

    // │────────────────────────────────────────│
    // │               cancel                   │
    // │────────────────────────────────────────│

    @Override
    public FindocResponse cancel(Long id) {

        Findoc f = getFindocOr404(id);

        if (f.getStatus() != DocumentStatus.POSTED)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Only POSTED documents can be cancelled");

        f.setStatus(DocumentStatus.CANCELLED);

        return toResponse(findocRepo.save(f));
    }

    // │────────────────────────────────────────│
    // │                get                     │
    // │────────────────────────────────────────│

    @Override
    public FindocResponse get(Long id) {
        return toResponse(getFindocOr404(id));
    }

    // │────────────────────────────────────────│
    // │                list                    │
    // │────────────────────────────────────────│

    @Override
    public Page<FindocResponse> list(Pageable pageable, DocumentStatus status) {
        if (status == null)
            return findocRepo.findAll(pageable).map(this::toResponse);

        return findocRepo.findAllByStatus(status, pageable)
                .map(this::toResponse);
    }

    // │────────────────────────────────────────│
    // │              helpers                   │
    // │────────────────────────────────────────│

    private BigDecimal nz(BigDecimal b) {
        return b == null ? BigDecimal.ZERO : b;
    }

    private void recalcTotals(Findoc f) {
        List<Mtrlines> lines = lineRepo.findByFindocIdOrderByLineNoAsc(f.getId());

        BigDecimal net = BigDecimal.ZERO;
        BigDecimal vat = BigDecimal.ZERO;
        BigDecimal gross = BigDecimal.ZERO;

        for (Mtrlines l : lines) {
            net = net.add(nz(l.getNetAmount()));
            vat = vat.add(nz(l.getVatAmount()));
            gross = gross.add(nz(l.getTotalAmount()));
        }

        f.setTotalNet(net);
        f.setTotalVat(vat);
        f.setTotalGross(gross);
    }

    private FindocResponse toResponse(Findoc f) {

        List<Mtrlines> lines =
                lineRepo.findByFindocIdOrderByLineNoAsc(f.getId());

        List<MtrLineResponse> lineDtos = new ArrayList<>();

        lines.stream()
                .sorted(Comparator.comparing(Mtrlines::getLineNo))
                .forEach(l -> {

                    Mtrl m = l.getMtrl();
                    Vat v = l.getVat();

                    lineDtos.add(new MtrLineResponse(
                            l.getId(),
                            l.getLineNo(),
                            m != null ? m.getId() : null,
                            m != null ? m.getCode() : null,
                            m != null ? m.getName() : null,
                            v != null ? v.getId() : null,
                            v != null ? v.getRate() : null,
                            l.getMtrUnit() != null ? l.getMtrUnit().getId() : null,

                            l.getWhouse() != null ? l.getWhouse().getId() : null,
                            l.getQty(),
                            l.getPrice(),
                            l.getDiscountRate(),
                            l.getNetAmount(),
                            l.getVatAmount(),
                            l.getTotalAmount()
                    ));

                });


        PaymentMethod pay = f.getPaymentMethod();
        ShipKind sk = f.getShipKind();

        MtrdocResponse mtrdocDto = null;
        if (f.getMtrdoc() != null) {
            Mtrdoc d = f.getMtrdoc();
            mtrdocDto = new MtrdocResponse(
                    d.getId(),
                    f.getId(),
                    d.getAddressLine1(),
                    d.getAddressLine2(),
                    d.getCity(),
                    d.getRegion(),
                    d.getPostalCode(),
                    d.getCountryCode(),
                    d.getWhouse() != null ? d.getWhouse().getId() : null,
                    d.getWhouse() != null ? d.getWhouse().getCode() : null,
                    d.getWhouse() != null ? d.getWhouse().getName() : null
            );
        }

        return new FindocResponse(
                f.getId(),
                f.getCompanyId(),
                f.getDocumentType() != null ? f.getDocumentType().getId() : null,
                f.getBranch() != null ? f.getBranch().getId() : null,
                f.getSeries() != null ? f.getSeries().getId() : null,
                f.getDocumentDomain(),
                f.getFiscalYear(),
                f.getNumber(),
                f.getPrintedNumber(),
                f.getDocumentDate(),
                f.getTrader() != null ? f.getTrader().getId() : null,
                f.getStatus() != null ? f.getStatus().getCode() : null,
                f.getTotalNet(),
                f.getTotalVat(),
                f.getTotalGross(),
                f.getCreatedAt(),
                f.getUpdatedAt(),
                // payment
                pay != null ? pay.getId() : null,
                pay != null ? pay.getCode() : null,
                pay != null ? pay.getDescription() : null,

                // shipkind
                sk != null ? sk.getId() : null,
                sk != null ? sk.getCode() : null,
                sk != null ? sk.getName() : null,

                mtrdocDto,
                lineDtos

        );
    }
    @Override
    public FindocResponse updateMtrdoc(Long findocId, MtrdocRequest req) {

        Findoc f = getFindocOr404(findocId);

        if (f.getStatus() != DocumentStatus.DRAFT)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only draft documents editable");

        Mtrdoc d = f.getMtrdoc();
        if (d == null) {
            d = new Mtrdoc();
            d.setFindoc(f);
        }

        d.setAddressLine1(req.addressLine1());
        d.setAddressLine2(req.addressLine2());
        d.setCity(req.city());
        d.setRegion(req.region());
        d.setPostalCode(req.postalCode());
        d.setCountryCode(req.countryCode());

        if (req.whouseId() != null) {
            Whouse wh = getWhouse(req.whouseId());
            d.setWhouse(wh);
        } else {
            d.setWhouse(null);
        }

        mtrdocRepo.save(d);
        f.setMtrdoc(d);

        return toResponse(findocRepo.save(f));
    }

}