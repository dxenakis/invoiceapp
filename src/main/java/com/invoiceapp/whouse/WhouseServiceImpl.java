package com.invoiceapp.whouse;

import com.invoiceapp.branch.Branch;
import com.invoiceapp.branch.BranchRepository;
import com.invoiceapp.companyscope.RequireTenant;
import com.invoiceapp.whouse.dto.WhouseRequest;
import com.invoiceapp.whouse.dto.WhouseResponse;
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
public class WhouseServiceImpl implements WhouseService {

    private final WhouseRepository repo;
    private final BranchRepository branchRepo;

    public WhouseServiceImpl(WhouseRepository repo, BranchRepository branchRepo) {
        this.repo = repo;
        this.branchRepo = branchRepo;
    }

    private static WhouseResponse toDto(Whouse e) {
        Branch b = e.getBranch();
        Long branchId = b != null ? b.getId() : null;
        String branchCode = b != null ? b.getCode() : null;
        String branchName = b != null ? b.getName() : null;

        return new WhouseResponse(
                e.getId(),
                e.getCompanyId(),
                e.getCode(),
                e.getName(),
                e.getDescription(),
                e.getAddressLine1(),
                e.getAddressLine2(),
                e.getCity(),
                e.getRegion(),
                e.getPostalCode(),
                e.getCountryCode(),
                e.getPhone(),
                e.getEmail(),
                e.isHeadquarters(),
                e.isActive(),
                branchId,
                branchCode,
                branchName,
                e.getCreatedAt(),
                e.getUpdatedAt()
        );
    }

    private Branch loadBranch(Long branchId) {
        return branchRepo.findById(branchId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Branch not found: id=" + branchId
                ));
    }

    private void apply(Whouse e, WhouseRequest req) {
        e.setCode(req.code());
        e.setName(req.name());
        e.setDescription(req.description());
        e.setAddressLine1(req.addressLine1());
        e.setAddressLine2(req.addressLine2());
        e.setCity(req.city());
        e.setRegion(req.region());
        e.setPostalCode(req.postalCode());
        e.setCountryCode(req.countryCode());
        e.setPhone(req.phone());
        e.setEmail(req.email());
        if (req.headquarters() != null) e.setHeadquarters(req.headquarters());
        if (req.active() != null) e.setActive(req.active());

        if (req.branchId() != null) {
            Branch b = loadBranch(req.branchId());
            e.setBranch(b);
        }
    }

    @Override
    public WhouseResponse create(WhouseRequest req) {
        if (!StringUtils.hasText(req.code()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "code is required");
        if (!StringUtils.hasText(req.name()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name is required");
        if (req.branchId() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "branchId is required");
        if (repo.existsByCode(req.code()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "code already exists: " + req.code());

        Whouse e = new Whouse();
        apply(e, req);
        return toDto(repo.save(e));
    }

    @Override
    @Transactional(readOnly = true)
    public WhouseResponse getById(Long id) {
        return repo.findById(id)
                .map(WhouseServiceImpl::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Whouse not found: id=" + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WhouseResponse> list(Pageable pageable, Boolean onlyActive, Long branchId) {
        Page<Whouse> page;

        if (branchId != null) {
            if (Boolean.TRUE.equals(onlyActive)) {
                page = repo.findByBranchIdAndActiveTrue(branchId, pageable);
            } else {
                page = repo.findByBranchId(branchId, pageable);
            }
        } else if (Boolean.TRUE.equals(onlyActive)) {
            // όπως στο BranchServiceImpl: tenant filter εφαρμόζεται, το companyId μπορεί να είναι null
            page = repo.findByCompanyIdAndActiveTrue(null, pageable);
        } else {
            page = repo.findAll(pageable);
        }

        return page.map(WhouseServiceImpl::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WhouseResponse> listByBranch(Long branchId, Pageable pageable, Boolean onlyActive) {
        if (branchId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "branchId is required");
        }

        Page<Whouse> page;
        if (Boolean.TRUE.equals(onlyActive)) {
            page = repo.findByBranchIdAndActiveTrue(branchId, pageable);
        } else {
            page = repo.findByBranchId(branchId, pageable);
        }

        return page.map(WhouseServiceImpl::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WhouseResponse> listByCompany(Long companyId, Pageable pageable, Boolean onlyActive, Long branchId) {
        if (companyId == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "companyId is required");

        Page<Whouse> page;

        if (branchId != null) {
            if (Boolean.TRUE.equals(onlyActive)) {
                page = repo.findByCompanyIdAndBranchIdAndActiveTrue(companyId, branchId, pageable);
            } else {
                page = repo.findByCompanyIdAndBranchId(companyId, branchId, pageable);
            }
        } else {
            if (Boolean.TRUE.equals(onlyActive)) {
                page = repo.findByCompanyIdAndActiveTrue(companyId, pageable);
            } else {
                page = repo.findByCompanyId(companyId, pageable);
            }
        }

        return page.map(WhouseServiceImpl::toDto);
    }

    @Override
    public WhouseResponse update(Long id, WhouseRequest req) {
        Whouse e = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Whouse not found: id=" + id));

        if (StringUtils.hasText(req.code()) && !req.code().equals(e.getCode())) {
            if (repo.existsByCode(req.code()))
                throw new ResponseStatusException(HttpStatus.CONFLICT, "code already exists: " + req.code());
            e.setCode(req.code());
        }

        if (StringUtils.hasText(req.name())) e.setName(req.name());
        e.setDescription(req.description());
        e.setAddressLine1(req.addressLine1());
        e.setAddressLine2(req.addressLine2());
        e.setCity(req.city());
        e.setRegion(req.region());
        e.setPostalCode(req.postalCode());
        e.setCountryCode(req.countryCode());
        e.setPhone(req.phone());
        e.setEmail(req.email());
        if (req.headquarters() != null) e.setHeadquarters(req.headquarters());
        if (req.active() != null) e.setActive(req.active());

        if (req.branchId() != null) {
            Branch b = loadBranch(req.branchId());
            e.setBranch(b);
        }

        return toDto(e); // managed entity
    }

    @Override
    public void delete(Long id) {
        Whouse e = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Whouse not found: id=" + id));
        // soft delete
        e.setActive(false);
    }
}
