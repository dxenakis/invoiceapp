package com.invoiceapp.mtrl;


import com.invoiceapp.companyscope.RequireTenant;
import com.invoiceapp.documenttype.DocumentType;
import com.invoiceapp.documenttype.DocumentTypeServiceImpl;
import com.invoiceapp.documenttype.dto.DocumentTypeRequest;
import com.invoiceapp.documenttype.dto.DocumentTypeResponse;
import com.invoiceapp.iteprms.ItePrms;
import com.invoiceapp.mtrl.dto.MtrlRequest;
import com.invoiceapp.mtrl.dto.MtrlResponse;
import com.invoiceapp.tprms.Tprms;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;


@Service
@Transactional
@RequireTenant // Εξασφαλίζει ότι υπάρχει active tenant (companyId) σε ΟΛΕΣ τις μεθόδους του service
public class MtrlServiceImpl implements MtrlService {


    private final MtrlRepository repo;


    public MtrlServiceImpl(MtrlRepository repo) {
        this.repo = repo;
    }


    // ---------- Mapping ----------
    private static MtrlResponse toDto(Mtrl mtrl) {
        return new MtrlResponse(
                mtrl.getId(),
                mtrl.getCompanyId(),
                mtrl.getCode(),
                mtrl.getName(),
                mtrl.getName1(),
                mtrl.getAccountCategory(),
                mtrl.getPricer(),
                mtrl.getPricew(),
                mtrl.isActive(),
                mtrl.getCreatedAt(),
                mtrl.getUpdatedAt()
        );
    }
    private static void apply(Mtrl mtrl, MtrlRequest req) {
        mtrl.setCode(req.code());
        mtrl.setName(req.name());
        mtrl.setName1(req.name1());
        mtrl.setAccountCategory(req.accountCategory());
        mtrl.setPricer(req.pricer());
        mtrl.setPricew(req.pricew());
        mtrl.setActive(req.active());
        mtrl.setCreatedAt(req.createdAt());
        mtrl.setUpdatedAt(req.updatedAt());
    }




    @Override
    @Transactional(readOnly = true)
    public Page<MtrlResponse> listAll(Pageable pageable) {
// Το Hibernate εφαρμόζει αυτόματα το tenant filter (μέσω @TenantId στο Item)
        return repo.findAll(pageable)
                .map(MtrlServiceImpl::toDto);

    }



    @Override
    @Transactional(readOnly = true)
    public MtrlResponse getById(Long id) {
        return repo.findById(id)
                .map(MtrlServiceImpl::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
    }


    @Override
    public MtrlResponse create(MtrlRequest req) {
// Μην πειράζεις companyId εδώ· με @TenantId θα συμπληρωθεί από το Session.
// Αν το entity σου ΔΕΝ έχει @TenantId, πρόσθεσέ το εκεί (π.χ. private Long companyId με @TenantId).


        if (!StringUtils.hasText(req.code()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "code is required");
        if (!StringUtils.hasText(req.name()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name is required");
        if (repo.existsByCode(req.code()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "code already exists: " + req.code());
        Mtrl mtrl = new Mtrl();
        apply(mtrl, req);
        return toDto(repo.save(mtrl));
    }


    @Override
    public MtrlResponse update(Long id, MtrlRequest req) {
        Mtrl existing = repo.getById(id); // already tenant-filtered
        existing.setName(req.name());

        return toDto(existing);
    }

    @Override
    public void delete(Long id){
        repo.deleteById(id);
    };
}