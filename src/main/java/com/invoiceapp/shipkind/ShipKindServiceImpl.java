package com.invoiceapp.shipkind;

import com.invoiceapp.shipkind.dto.ShipKindRequest;
import com.invoiceapp.shipkind.dto.ShipKindResponse;
import com.invoiceapp.shipkind.enums.MyDataShipKind;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class ShipKindServiceImpl implements ShipKindService {

    private final ShipKindRepository repo;

    public ShipKindServiceImpl(ShipKindRepository repo) {
        this.repo = repo;
    }

    private static ShipKindResponse toDto(ShipKind e) {
        MyDataShipKind md = e.getMydataShipKind();
        Integer myCode = md != null ? md.getCode() : null;
        String myLabel = md != null ? md.getLabelEl() : null;

        return new ShipKindResponse(
                e.getId(),
                e.getCode(),
                e.getName(),
                e.getName1(),
                myCode,
                myLabel,
                e.isActive(),
                e.getCreatedAt(),
                e.getUpdatedAt()
        );
    }

    @Override
    public ShipKindResponse create(ShipKindRequest req) {
        String codeTrimmed = req.code().trim();
        if (repo.existsByCode(codeTrimmed)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "ShipKind code already exists: " + req.code());
        }

        ShipKind e = new ShipKind();
        e.setCode(codeTrimmed);
        e.setName(req.name());
        e.setName1(req.name1());

        if (req.mydataShipKindCode() != null) {
            e.setMydataShipKind(MyDataShipKind.fromCode(req.mydataShipKindCode()));
        }

        if (req.active() != null) {
            e.setActive(req.active());
        }

        ShipKind saved = repo.save(e);
        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ShipKindResponse get(Long id) {
        ShipKind e = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ShipKind not found: id=" + id));
        return toDto(e);
    }

    @Override
    @Transactional(readOnly = true)
    public ShipKindResponse getByCode(String code) {
        ShipKind e = repo.findByCode(code.trim())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ShipKind not found: code=" + code));
        return toDto(e);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ShipKindResponse> list(Pageable pageable, Boolean onlyActive) {
        Page<ShipKind> page = (onlyActive != null && onlyActive)
                ? repo.findByActiveTrue(pageable)
                : repo.findAll(pageable);

        return page.map(ShipKindServiceImpl::toDto);
    }

    @Override
    public ShipKindResponse update(Long id, ShipKindRequest req) {
        ShipKind e = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ShipKind not found: id=" + id));

        if (StringUtils.hasText(req.code())) {
            String newCode = req.code().trim();
            if (!newCode.equals(e.getCode()) && repo.existsByCode(newCode)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "ShipKind code already exists: " + newCode);
            }
            e.setCode(newCode);
        }

        if (StringUtils.hasText(req.name())) {
            e.setName(req.name());
        }

        if (req.name1() != null) {
            e.setName1(req.name1());
        }

        if (req.mydataShipKindCode() != null) {
            e.setMydataShipKind(MyDataShipKind.fromCode(req.mydataShipKindCode()));
        }

        if (req.active() != null) {
            e.setActive(req.active());
        }

        // thanks to @Transactional ο entity θα γίνει flush μόνος του
        return toDto(e);
    }

    @Override
    public void delete(Long id) {
        ShipKind e = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ShipKind not found: id=" + id));
        repo.delete(e);
    }
}
