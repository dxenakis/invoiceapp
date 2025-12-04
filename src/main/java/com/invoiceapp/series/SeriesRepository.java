package com.invoiceapp.series;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeriesRepository extends JpaRepository<Series, Long> {

    boolean existsByDocumentTypeIdAndBranchIdAndCode(Long documentTypeId, Long branchId, String code);
    boolean existsByDocumentTypeIdAndBranchIdAndCodeAndWhouseId(Long documentTypeId, Long branchId, String code,Long whouseId);

    Optional<Series> findByDocumentTypeIdAndBranchIdAndCode(Long documentTypeId, Long branchId, String code);
    Optional<Series> findByDocumentTypeIdAndBranchIdAndCodeAndWhouseId(Long documentTypeId, Long branchId, String code,Long whouseId);
}
