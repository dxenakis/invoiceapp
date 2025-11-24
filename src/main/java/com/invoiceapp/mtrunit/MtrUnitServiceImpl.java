package com.invoiceapp.mtrunit;

import com.invoiceapp.mtrunit.dto.MtrUnitRequest;
import com.invoiceapp.mtrunit.dto.MtrUnitResponse;
import com.invoiceapp.mtrunit.enums.MyDataMtrUnitCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class MtrUnitServiceImpl implements MtrUnitService {

    private final MtrUnitRepository repo;

    public MtrUnitServiceImpl(MtrUnitRepository repo) {
        this.repo = repo;
    }

    private static MtrUnitResponse toDto(MtrUnit e) {
        MyDataMtrUnitCode md = e.getMydataCode();
        Integer myCode = md != null ? md.getCode() : null;
        String myLabel = md != null ? md.getLabelEl() : null;

        return new MtrUnitResponse(
                e.getId(),
                e.getCode(),
                e.getName(),
                e.getName1(),
                e.getIsoCode(),
                myCode,
                myLabel,
                e.isActive(),
                e.getCreatedAt(),
                e.getUpdatedAt()
        );
    }

    @Override
    public MtrUnitResponse create(MtrUnitRequest req) {
        if (repo.existsByCode(req.code().trim())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "MtrUnit code already exists: " + req.code());
        }

        MtrUnit e = new MtrUnit();
        e.setCode(req.code());
        e.setName(req.name());
        e.setName1(req.name1());
        e.setIsoCode(req.isoCode());

        if (req.mydataCode() != null) {
            e.setMydataCode(MyDataMtrUnitCode.fromCode(req.mydataCode()));
        }

        if (req.active() != null) {
            e.setActive(req.active());
        }

        repo.save(e);
        return toDto(e);
    }

    @Override
    @Transactional(readOnly = true)
    public MtrUnitResponse get(Long id) {
        MtrUnit e = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MtrUnit not found: id=" + id));
        return toDto(e);
    }

    @Override
    @Transactional(readOnly = true)
    public MtrUnitResponse getByCode(String code) {
        MtrUnit e = repo.findByCode(code.trim())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MtrUnit not found: code=" + code));
        return toDto(e);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MtrUnitResponse> list(Pageable pageable, Boolean onlyActive) {
        Page<MtrUnit> page = (onlyActive != null && onlyActive)
                ? repo.findByActiveTrue(pageable)
                : repo.findAll(pageable);

        return page.map(MtrUnitServiceImpl::toDto);
    }

    @Override
    public MtrUnitResponse update(Long id, MtrUnitRequest req) {
        MtrUnit e = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MtrUnit not found: id=" + id));

        if (StringUtils.hasText(req.code())) {
            String newCode = req.code().trim();
            if (!newCode.equals(e.getCode()) && repo.existsByCode(newCode)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "MtrUnit code already exists: " + newCode);
            }
            e.setCode(newCode);
        }

        if (StringUtils.hasText(req.name())) {
            e.setName(req.name());
        }

        if (req.name1() != null) {
            e.setName1(req.name1());
        }

        if (req.isoCode() != null) {
            e.setIsoCode(req.isoCode());
        }

        if (req.mydataCode() != null) {
            e.setMydataCode(MyDataMtrUnitCode.fromCode(req.mydataCode()));
        } else if (req.mydataCode() == null) {
            // αν θες να σβήνεις τη σχέση όταν έρχεται null
            // e.setMydataCode(null);
        }

        if (req.active() != null) {
            e.setActive(req.active());
        }

        return toDto(e);
    }

    @Override
    public void delete(Long id) {
        MtrUnit e = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MtrUnit not found: id=" + id));
        repo.delete(e);
    }
}
