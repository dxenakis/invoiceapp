package com.invoiceapp.iteprms;

import com.invoiceapp.iteprms.dto.ItePrmsCreateRequest;
import com.invoiceapp.iteprms.dto.ItePrmsUpdateRequest;
import com.invoiceapp.tprms.Tprms;
import com.invoiceapp.tprms.TprmsRepository;
import com.invoiceapp.tprms.dto.TprmsCreateRequest;
import com.invoiceapp.tprms.dto.TprmsUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ItePrmsServiceImpl implements ItePrmsService{

    private final ItePrmsRepository repo;

    public ItePrmsServiceImpl(ItePrmsRepository repo) {
        this.repo = repo;
    }

    @Override
    public ItePrms create(ItePrmsCreateRequest req) {
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


        ItePrms e = new ItePrms();
        e.setCode(req.code());
        e.setDescription(req.description());
        e.setDomain(req.domain());
        e.setImpqty(req.impqty());
        e.setImpval(req.impval());
        e.setExpqty(req.expqty());
        e.setExpval(req.expval());
        e.setPurqty(req.purqty());
        e.setPurval(req.purval());
        e.setSalqty(req.salqty());
        e.setSalval(req.salval());
        if (req.active() != null) e.setActive(req.active());

        return repo.save(e);
    }
    @Override
    @Transactional(readOnly = true)
    public Page<ItePrms> list(Pageable pageable, boolean onlyActive) {
        return onlyActive ? repo.findAllByActiveTrue(pageable) : repo.findAll(pageable);
    }
    @Override
    @Transactional(readOnly = true)
    public ItePrms get(Long id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("ITEPRMS not found: id=" + id));
    }
    @Override
    public ItePrms update(Long id, ItePrmsUpdateRequest req) {
        ItePrms e = get(id);

        if (req.description() != null && !req.description().isBlank()) {
            e.setDescription(req.description());
        }
        if (req.domain() != null) {
            e.setDomain(req.domain());
        }
        if (req.impqty() != null) {
            e.setImpqty(req.impqty());
        }
        if (req.impval() != null) {
            e.setImpval(req.impval());
        }
        if (req.expqty() != null) {
            e.setExpqty(req.expqty());
        }
        if (req.expval() != null) {
            e.setExpval(req.expval());
        }

        if (req.purqty() != null) {
            e.setPurqty(req.purqty());
        }
        if (req.purval() != null) {
            e.setPurval(req.purval());
        }
        if (req.salqty() != null) {
            e.setSalqty(req.salqty());
        }
        if (req.salval() != null) {
            e.setSalval(req.salval());
        }


        if (req.active() != null) {
            e.setActive(req.active());
        }

        return e; // managed entity, θα γίνει flush στο commit
    }

    @Override
    public void delete(Long id) {
        ItePrms e = get(id);
        e.setActive(false);
    }
}








