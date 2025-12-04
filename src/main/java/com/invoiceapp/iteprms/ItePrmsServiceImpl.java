package com.invoiceapp.iteprms;

import com.invoiceapp.findoc.enums.DocumentDomain;
import com.invoiceapp.global.enums.Effect;
import com.invoiceapp.iteprms.dto.ItePrmsCreateRequest;
import com.invoiceapp.iteprms.dto.ItePrmsUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        e.setDomain(DocumentDomain.fromCode(req.domain()));
        e.setImpqty(Effect.fromCode(req.impqty()));
        e.setImpval(Effect.fromCode(req.impval()));
        e.setExpqty(Effect.fromCode(req.expqty()));
        e.setExpval(Effect.fromCode(req.expval()));
        e.setPurqty(Effect.fromCode(req.purqty()));
        e.setPurval(Effect.fromCode(req.purval()));
        e.setSalqty(Effect.fromCode(req.salqty()));
        e.setSalval(Effect.fromCode(req.salval()));
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
    @Transactional
    public List<ItePrms> findAllByDomain(DocumentDomain documentDomain){
        return  repo.findAllByDomain(documentDomain);
    }

    @Override
    public ItePrms update(Long id, ItePrmsUpdateRequest req) {
        ItePrms e = get(id);

        if (req.description() != null && !req.description().isBlank()) {
            e.setDescription(req.description());
        }
        if (req.domain() != null) {
            e.setDomain(DocumentDomain.fromCode(req.domain()));
        }
        if (req.impqty() != null) {
            e.setImpqty(Effect.fromCode(req.impqty()));
        }
        if (req.impval() != null) {
            e.setImpval(Effect.fromCode(req.impval()));
        }
        if (req.expqty() != null) {
            e.setExpqty(Effect.fromCode(req.expqty()));
        }
        if (req.expval() != null) {
            e.setExpval(Effect.fromCode(req.expval()));
        }

        if (req.purqty() != null) {
            e.setPurqty(Effect.fromCode(req.purqty()));
        }
        if (req.purval() != null) {
            e.setPurval(Effect.fromCode(req.purval()));
        }
        if (req.salqty() != null) {
            e.setSalqty(Effect.fromCode(req.salqty()));
        }
        if (req.salval() != null) {
            e.setSalval(Effect.fromCode(req.salval()));
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








