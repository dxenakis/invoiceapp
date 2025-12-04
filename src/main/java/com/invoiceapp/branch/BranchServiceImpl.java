package com.invoiceapp.branch;

import com.invoiceapp.branch.dto.BranchRequest;
import com.invoiceapp.branch.dto.BranchResponse;
import com.invoiceapp.companyscope.RequireTenant;
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
@RequireTenant
public class BranchServiceImpl implements BranchService {

    private final BranchRepository repo;

    public BranchServiceImpl(BranchRepository repo) {
        this.repo = repo;
    }

    private static BranchResponse toDto(Branch e) {
        return new BranchResponse(
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
                e.getCreatedAt(),
                e.getUpdatedAt()
        );
    }

    private static void apply(Branch e, BranchRequest req) {
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
    }

    @Override
    public BranchResponse create(BranchRequest req) {
        if (!StringUtils.hasText(req.code()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "code is required");
        if (!StringUtils.hasText(req.name()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name is required");
        if (repo.existsByCode(req.code()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "code already exists: " + req.code());

        Branch e = new Branch();
        apply(e, req);
        return toDto(repo.save(e));
    }

    @Override
    @Transactional(readOnly = true)
    public BranchResponse getById(Long id) {
        return repo.findById(id)
                .map(BranchServiceImpl::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Branch not found: id=" + id));
    }

    @Override
    @Transactional(readOnly = true)
    public BranchResponse getByCode(String code) {
        return repo.findByCode(code)
                .map(BranchServiceImpl::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Branch not found: code=" + code));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BranchResponse> list(Pageable pageable, Boolean onlyActive) {
        if (Boolean.TRUE.equals(onlyActive)) {
            return repo.findAll(pageable) // tenant filter applies; companyId ignored
                    .map(BranchServiceImpl::toDto);
        }
        return repo.findAll(pageable).map(BranchServiceImpl::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BranchResponse> listByCompany(Long companyId, Pageable pageable, Boolean onlyActive) {
        if (companyId == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "companyId is required");

        if (Boolean.TRUE.equals(onlyActive)) {
            return repo.findByCompanyIdAndActiveTrue(companyId, pageable).map(BranchServiceImpl::toDto);
        }
        return repo.findByCompanyId(companyId, pageable).map(BranchServiceImpl::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BranchResponse> listHeadquarters(Long companyId) {
        if (companyId == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "companyId is required");
        return repo.findByCompanyIdAndHeadquartersTrue(companyId)
                .stream().map(BranchServiceImpl::toDto).toList();
    }

    @Override
    public BranchResponse update(Long id, BranchRequest req) {
        Branch e = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Branch not found: id=" + id));

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

        return toDto(e); // managed entity
    }

    @Override
    public void delete(Long id) {
        Branch e = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Branch not found: id=" + id));
        // Soft delete
        e.setActive(false);
    }
}
