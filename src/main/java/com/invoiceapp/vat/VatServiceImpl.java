package com.invoiceapp.vat;

import com.invoiceapp.vat.dto.VatRequest;
import com.invoiceapp.vat.dto.VatResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import com.invoiceapp.vat.enums.MyDataVatCode;

import java.math.BigDecimal;

@Service
@Transactional
public class VatServiceImpl implements VatService {

    private final VatRepository repo;

    public VatServiceImpl(VatRepository repo) {
        this.repo = repo;
    }

    private static VatResponse toDto(Vat e) {
        MyDataVatCode my = e.getMydataVatCode();

        Integer myCode = my != null ? my.getCode() : null;
        String myLabel = my != null ? my.getLabelEl() : null;
        Integer myPercent = my != null ? my.getPercent() : null;

        return new VatResponse(
                e.getId(),
                e.getCode(),
                e.getDescription(),
                e.getRate(),
                myCode,
                myLabel,
                myPercent,
                e.isActive(),
                e.getCreatedAt(),
                e.getUpdatedAt()
        );
    }


    @Override
    public VatResponse create(VatRequest req) {
        if (!StringUtils.hasText(req.code())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "code is required");
        if (req.rate() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "rate is required");
        if (req.rate().compareTo(BigDecimal.ZERO) < 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "rate must be >= 0");
        if (repo.existsByCode(req.code())) throw new ResponseStatusException(HttpStatus.CONFLICT, "code already exists");

        Vat e = new Vat();
        e.setCode(req.code());
        e.setDescription(req.description());
        e.setRate(req.rate());
        if (req.mydataVatCode() != null) {
            e.setMydataVatCode(MyDataVatCode.fromCode(req.mydataVatCode()));
        } else {
            e.setMydataVatCode(null);
        }
        if (req.active() != null) e.setActive(req.active());
        return toDto(repo.save(e));
    }

    @Override @Transactional(readOnly = true)
    public VatResponse get(Long id) {
        return repo.findById(id).map(VatServiceImpl::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vat not found: id=" + id));
    }

    @Override @Transactional(readOnly = true)
    public VatResponse getByCode(String code) {
        return repo.findByCode(code).map(VatServiceImpl::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vat not found: code=" + code));
    }

    @Override @Transactional(readOnly = true)
    public Page<VatResponse> list(Pageable pageable, Boolean onlyActive) {
        return repo.findAll(pageable).map(VatServiceImpl::toDto);
    }

    @Override
    public VatResponse update(Long id, VatRequest req) {
        Vat e = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vat not found: id=" + id));

        if (StringUtils.hasText(req.code()) && !req.code().equals(e.getCode())) {
            if (repo.existsByCode(req.code())) throw new ResponseStatusException(HttpStatus.CONFLICT, "code already exists");
            e.setCode(req.code());
        }
        if (req.description() != null) e.setDescription(req.description());
        if (req.rate() != null) {
            if (req.rate().compareTo(BigDecimal.ZERO) < 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "rate must be >= 0");
            e.setRate(req.rate());
        }

        if (req.mydataVatCode() != null) {
            e.setMydataVatCode(MyDataVatCode.fromCode(req.mydataVatCode()));
        } else {
            // αν θέλεις να το “καθαρίζεις” όταν έρχεται null
            e.setMydataVatCode(null);
        }
        if (req.active() != null) e.setActive(req.active());

        return toDto(e);
    }

    @Override
    public void delete(Long id) {
        Vat e = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vat not found: id=" + id));
        repo.delete(e);
    }
}
