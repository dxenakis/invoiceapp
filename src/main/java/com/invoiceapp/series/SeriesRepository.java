package com.invoiceapp.series;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeriesRepository extends JpaRepository<Series, Long> {

    boolean existsByDocumentTypeIdAndBranchIdAndCode(Long documentTypeId, Long branchId, String code);

    Optional<Series> findByDocumentTypeIdAndBranchIdAndCode(Long documentTypeId, Long branchId, String code);
}
