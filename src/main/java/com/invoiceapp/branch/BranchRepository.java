package com.invoiceapp.branch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch, Long> {

    // Μοναδικότητα ανά tenant λόγω @TenantId
    boolean existsByCode(String code);

    Optional<Branch> findByCode(String code);
    Page<Branch> findAll(Pageable pageable);
    // Βασικό ζητούμενο: εύρεση ανά εταιρεία
    List<Branch> findByCompanyId(Long companyId);
    Page<Branch> findByCompanyId(Long companyId, Pageable pageable);

    // Φιλτραρισμένα
    Page<Branch> findByCompanyIdAndActiveTrue(Long companyId, Pageable pageable);

    List<Branch> findByCompanyIdAndHeadquartersTrue(Long companyId);
}
