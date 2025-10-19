package com.invoiceapp.tprms;

import com.invoiceapp.global.DocumentDomain;
import com.invoiceapp.global.Effect;
import com.invoiceapp.global.Sign;
import com.invoiceapp.tprms.dto.TprmsCreateRequest;
import com.invoiceapp.tprms.dto.TprmsUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TprmsServiceImpl implements TprmsService {

    private final TprmsRepository repo;

    public TprmsServiceImpl(TprmsRepository repo) {
        this.repo = repo;
    }

    @Override
    public Tprms create(TprmsCreateRequest req) {
        // Βασικά validations
        if (req.code() == null || req.code().isBlank()) {
            throw new IllegalArgumentException("code is required");
        }
        if (repo.existsByCode(req.code())) {
            throw new IllegalArgumentException("code already exists: " + req.code());
        }
        if (req.description() == null || req.description().isBlank()) {
            throw new IllegalArgumentException("description is required");
        }
        if (req.domain() == null) {
            throw new IllegalArgumentException("domain is required");
        }
        if (req.debit() == null || req.credit() == null || req.turnover() == null) {
            throw new IllegalArgumentException("effects (debit/credit/turnover) are required");
        }
        if (req.sign() == null) {
            throw new IllegalArgumentException("sign is required");
        }

        Tprms e = new Tprms();
        e.setCode(req.code());
        e.setDescription(req.description());
        e.setDomain(req.domain());
        e.setDebit(req.debit());
        e.setCredit(req.credit());
        e.setTurnover(req.turnover());
        e.setSign(req.sign());
        if (req.active() != null) e.setActive(req.active());

        return repo.save(e);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Tprms> list(Pageable pageable, boolean onlyActive) {
        return onlyActive ? repo.findAllByActiveTrue(pageable) : repo.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Tprms get(Long id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("TPRMS not found: id=" + id));
    }

    @Override
    public Tprms update(Long id, TprmsUpdateRequest req) {
        Tprms e = get(id);

        if (req.description() != null && !req.description().isBlank()) {
            e.setDescription(req.description());
        }
        if (req.domain() != null) {
            e.setDomain(req.domain());
        }
        if (req.debit() != null) {
            e.setDebit(req.debit());
        }
        if (req.credit() != null) {
            e.setCredit(req.credit());
        }
        if (req.turnover() != null) {
            e.setTurnover(req.turnover());
        }
        if (req.sign() != null) {
            e.setSign(req.sign());
        }
        if (req.active() != null) {
            e.setActive(req.active());
        }

        return e; // managed entity, θα γίνει flush στο commit
    }

    @Override
    public void delete(Long id) {
        Tprms e = get(id);
        e.setActive(false);
    }
}
