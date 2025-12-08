package com.invoiceapp.documenttype;

import com.invoiceapp.companyscope.RequireTenant;
import com.invoiceapp.documenttype.dto.DocumentTypeRequest;
import com.invoiceapp.documenttype.dto.DocumentTypeResponse;
import com.invoiceapp.findoc.enums.DocumentDomain;
import com.invoiceapp.iteprms.ItePrms;
import com.invoiceapp.iteprms.ItePrmsRepository;
import com.invoiceapp.tprms.Tprms;
import com.invoiceapp.tprms.TprmsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
@RequireTenant
public class DocumentTypeServiceImpl implements DocumentTypeService {

    private final DocumentTypeRepository repository;
    private final TprmsRepository tprmsRepository;
    private final ItePrmsRepository itePrmsRepository;

    public DocumentTypeServiceImpl(DocumentTypeRepository repository,
                                   TprmsRepository tprmsRepository,
                                   ItePrmsRepository itePrmsRepository) {
        this.repository = repository;
        this.tprmsRepository = tprmsRepository;
        this.itePrmsRepository = itePrmsRepository;
    }

    // ---------- Mapping ----------
    private static DocumentTypeResponse toDto(DocumentType e) {
        return new DocumentTypeResponse(
                e.getId(),
                e.getCompanyId(),
                e.getCode(),
                e.getDescription(),
                e.getDomain().getCode(),
                e.getTprms() != null ? e.getTprms().getId() : null,
                e.getTprms() != null ? e.getTprms().getCode() : null,
                e.getTprms() != null ? e.getTprms().getDescription() : null,
                e.getItePrms() != null ? e.getItePrms().getId() : null,
                e.getItePrms() != null ? e.getItePrms().getCode() : null,
                e.getItePrms() != null ? e.getItePrms().getDescription() : null,
                e.getCreatedAt(),
                e.getUpdatedAt()
        );
    }

    private static void apply(DocumentType e, DocumentTypeRequest req, Tprms tprms, ItePrms itePrms) {
        e.setCode(req.code());
        e.setDescription(req.description());
        e.setDomain(DocumentDomain.fromCode(req.domain()));
        e.setTprms(tprms);
        e.setItePrms(itePrms);
    }

    // ---------- CRUD ----------
    @Override
    @Transactional(readOnly = true)
    public DocumentTypeResponse getById(Long id) {
        return repository.findById(id)
                .map(DocumentTypeServiceImpl::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "DocumentType not found: id=" + id));
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentTypeResponse getByCode(String code) {
        return repository.findByCode(code)
                .map(DocumentTypeServiceImpl::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "DocumentType not found: code=" + code));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DocumentTypeResponse> list(Pageable pageable) {
        return repository.findAll(pageable).map(DocumentTypeServiceImpl::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DocumentTypeResponse> listByDomain(DocumentDomain domain, Pageable pageable) {
        return repository.findAllByDomain(domain, pageable).map(DocumentTypeServiceImpl::toDto);
    }

    @Override
    public DocumentTypeResponse create(DocumentTypeRequest request) {
        // validations
        if (!StringUtils.hasText(request.code()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "code is required");
        if (!StringUtils.hasText(request.description()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "description is required");
        if (request.domain() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "domain is required");
       /* if (request.tprmsId() == null)
           // throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "tprmsId is required");
      */
        if (repository.existsByCode(request.code()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "code already exists: " + request.code());

        // tprms -> ΠΡΟΑΙΡΕΤΙΚΟ
        Tprms tprms = null;
        if (request.tprmsId() != null) {
            tprms = tprmsRepository.findById(request.tprmsId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "TPRMS not found: id=" + request.tprmsId()
                    ));
        }

        // itePrms -> ΠΡΟΑΙΡΕΤΙΚΟ (ήδη το είχες σωστό)
        ItePrms itePrms = null;
        if (request.iteprmsId() != null) {
            itePrms = itePrmsRepository.findById(request.iteprmsId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "ItePrms not found: id=" + request.iteprmsId()
                    ));
        }

        DocumentType e = new DocumentType();
        apply(e, request, tprms, itePrms);

        return toDto(repository.save(e));
    }

    @Override
    public DocumentTypeResponse update(Long id, DocumentTypeRequest request) {
        DocumentType e = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "DocumentType not found: id=" + id));

        // αν αλλάζει ο code -> έλεγχος μοναδικότητας (στον tenant)
        if (StringUtils.hasText(request.code()) && !request.code().equals(e.getCode())) {
            if (repository.existsByCode(request.code()))
                throw new ResponseStatusException(HttpStatus.CONFLICT, "code already exists: " + request.code());
            e.setCode(request.code());
        }

        if (StringUtils.hasText(request.description())) e.setDescription(request.description());
        if (request.domain() != null) e.setDomain(DocumentDomain.fromCode(request.domain()));

        if (request.tprmsId() != null) {
            Tprms tprms = tprmsRepository.findById(request.tprmsId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "TPRMS not found: id=" + request.tprmsId()));
            e.setTprms(tprms);
        }

        if (request.iteprmsId() != null) {
            ItePrms itePrms = itePrmsRepository.findById(request.iteprmsId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "ItePrms not found: id=" + request.iteprmsId()));
            e.setItePrms(itePrms);
        }

        return toDto(e); // managed entity, θα γίνει flush στο commit
    }

    @Override
    public void delete(Long id) {
        DocumentType e = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "DocumentType not found: id=" + id));
        repository.delete(e);
    }
}
