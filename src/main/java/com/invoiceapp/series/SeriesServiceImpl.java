package com.invoiceapp.series;

import com.invoiceapp.branch.Branch;
import com.invoiceapp.branch.BranchRepository;
import com.invoiceapp.documenttype.DocumentType;
import com.invoiceapp.documenttype.DocumentTypeRepository;
import com.invoiceapp.series.dto.SeriesRequest;
import com.invoiceapp.series.dto.SeriesResponse;
import com.invoiceapp.companyscope.RequireTenant;
import com.invoiceapp.whouse.Whouse;
import com.invoiceapp.whouse.WhouseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import com.invoiceapp.series.Series;
import com.invoiceapp.documenttype.DocumentType;
import com.invoiceapp.branch.Branch;
import com.invoiceapp.whouse.Whouse;
import com.invoiceapp.series.dto.SeriesResponse;

@Service
@Transactional
@RequireTenant
public class SeriesServiceImpl implements SeriesService {

    private final SeriesRepository repo;
    private final DocumentTypeRepository documentTypeRepo;
    private final BranchRepository branchRepo;
    private final WhouseRepository whouseRepo;
    public SeriesServiceImpl(SeriesRepository repo, DocumentTypeRepository documentTypeRepo, BranchRepository branchRepo, WhouseRepository whouseRepo) {
        this.repo = repo;
        this.documentTypeRepo = documentTypeRepo;
        this.branchRepo = branchRepo;
        this.whouseRepo = whouseRepo;
    }

    private static SeriesResponse toDto(Series e) {
        // ------------- DocumentType -> nested DTO -------------
        DocumentType dt = e.getDocumentType();
        SeriesResponse.DocumentTypeNestedResponse dtDto = null;
        if (dt != null) {
            dtDto = new SeriesResponse.DocumentTypeNestedResponse(
                    dt.getId(),
                    dt.getCode(),
                    dt.getDescription()
            );
        }

        // ------------- Branch -> nested DTO -------------
        Branch br = e.getBranch();
        SeriesResponse.BranchNestedResponse brDto = null;
        if (br != null) {
            brDto = new SeriesResponse.BranchNestedResponse(
                    br.getId(),
                    br.getCode(),
                    br.getName()
            );
        }

        // ------------- Whouse -> nested DTO -------------
        Whouse wh = e.getWhouse();
        SeriesResponse.WhouseNestedResponse whDto = null;
        if (wh != null) {
            whDto = new SeriesResponse.WhouseNestedResponse(
                    wh.getId(),
                    wh.getCode(),
                    wh.getName()
            );
        }

        // ------------- Series -> SeriesResponse -------------
        return new SeriesResponse(
                e.getId(),
                e.getCompanyId(),
                e.getCode(),
                e.getDescription(),
                e.isActive(),
                e.getPrefix(),
                e.getFormatPattern(),
                e.getPaddingLength(),
                dtDto,
                brDto,
                whDto
        );
    }


    private void apply(Series e, SeriesRequest req, DocumentType dt, Branch br, Whouse wh) {
        e.setDocumentType(dt);
        e.setBranch(br);
        e.setCode(req.code());
        e.setDescription(req.description());
        e.setWhouse(wh);
        if (req.active() != null) e.setActive(req.active());
        e.setPrefix(req.prefix());
        e.setFormatPattern(req.formatPattern());
        if (req.paddingLength() != null) e.setPaddingLength(req.paddingLength());
    }

    @Override
    public SeriesResponse create(SeriesRequest req) {
        if (req.documentTypeId() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "documentTypeId is required");
        if (!StringUtils.hasText(req.code())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "code is required");

        DocumentType dt = documentTypeRepo.findById(req.documentTypeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "DocumentType not found: id=" + req.documentTypeId()));

        Branch br = null;
        if (req.branchId() != null) {
            br = branchRepo.findById(req.branchId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Branch not found: id=" + req.branchId()));
        }

        Long brId = br != null ? br.getId() : null;
        if (repo.existsByDocumentTypeIdAndBranchIdAndCode(dt.getId(), brId, req.code())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "series code already exists in scope");
        }

        Whouse wh = null;
        if (req.whouseId() != null) {
            wh = whouseRepo.findById(req.whouseId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Whouse not found: id=" + req.whouseId()));
        }

        Long whId = wh != null ? wh.getId() : null;
        if (repo.existsByDocumentTypeIdAndBranchIdAndCodeAndWhouseId(dt.getId(), brId, req.code(),whId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "series code already exists in scope");
        }


        Series e = new Series();
        apply(e, req, dt, br, wh);
        return toDto(repo.save(e));
    }

    @Override
    @Transactional(readOnly = true)
    public SeriesResponse getById(Long id) {
        return repo.findById(id).map(SeriesServiceImpl::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Series not found: id=" + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SeriesResponse> list(Pageable pageable) {
        return repo.findAll(pageable).map(SeriesServiceImpl::toDto);
    }

    @Override
    public SeriesResponse update(Long id, SeriesRequest req) {
        Series e = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Series not found: id=" + id));

        DocumentType dt = e.getDocumentType();
        Branch br = e.getBranch();
        Whouse wh = e.getWhouse();
        if (req.documentTypeId() != null && (dt == null || !req.documentTypeId().equals(dt.getId()))) {
            dt = documentTypeRepo.findById(req.documentTypeId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "DocumentType not found: id=" + req.documentTypeId()));
            e.setDocumentType(dt);
        }

        if (req.branchId() != null && (br == null || !req.branchId().equals(br.getId()))) {
            br = branchRepo.findById(req.branchId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Branch not found: id=" + req.branchId()));
            e.setBranch(br);
        }

        if(req.whouseId() !=null && (wh == null || !req.branchId().equals(wh.getId()))) {
            wh = whouseRepo.findById(req.whouseId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Whouse not found: id=" + req.whouseId()));
            e.setWhouse(wh);
        }

        if (StringUtils.hasText(req.code())) e.setCode(req.code());
        if (req.description() != null) e.setDescription(req.description());
        if (req.active() != null) e.setActive(req.active());
        if (req.prefix() != null) e.setPrefix(req.prefix());
        if (req.formatPattern() != null) e.setFormatPattern(req.formatPattern());
        if (req.paddingLength() != null) e.setPaddingLength(req.paddingLength());

        return toDto(e);
    }

    @Override
    public void delete(Long id) {
        Series e = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Series not found: id=" + id));
        repo.delete(e);
    }
}
