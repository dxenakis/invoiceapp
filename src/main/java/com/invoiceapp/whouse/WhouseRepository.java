package com.invoiceapp.whouse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WhouseRepository extends JpaRepository<Whouse, Long> {

    // Μοναδικότητα ανά tenant λόγω @TenantId
    boolean existsByCode(String code);

    // Βασικό ζητούμενο: εύρεση ανά εταιρεία (admin use-case)
    Page<Whouse> findByCompanyId(Long companyId, Pageable pageable);
    Page<Whouse> findByCompanyIdAndActiveTrue(Long companyId, Pageable pageable);

    // Για συγκεκριμένο υποκατάστημα
    Page<Whouse> findByBranchId(Long branchId, Pageable pageable);
    Page<Whouse> findByBranchIdAndActiveTrue(Long branchId, Pageable pageable);

    // Admin + φιλτράρισμα ανά εταιρεία *και* υποκατάστημα
    Page<Whouse> findByCompanyIdAndBranchId(Long companyId, Long branchId, Pageable pageable);
    Page<Whouse> findByCompanyIdAndBranchIdAndActiveTrue(Long companyId, Long branchId, Pageable pageable);
}
