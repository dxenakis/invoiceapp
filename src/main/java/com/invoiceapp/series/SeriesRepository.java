package com.invoiceapp.series;

import com.invoiceapp.documenttype.DocumentType;
import com.invoiceapp.findoc.enums.DocumentDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeriesRepository extends JpaRepository<Series, Long> {

    boolean existsByDocumentTypeIdAndBranchIdAndCode(Long documentTypeId, Long branchId, String code);
    boolean existsByDocumentTypeIdAndBranchIdAndCodeAndWhouseId(Long documentTypeId, Long branchId, String code,Long whouseId);
    Page<Series> findAllByDomain(DocumentDomain domain, Pageable pageable);
    Optional<Series> findByDocumentTypeIdAndBranchIdAndCode(Long documentTypeId, Long branchId, String code);
    Optional<Series> findByDocumentTypeIdAndBranchIdAndCodeAndWhouseId(Long documentTypeId, Long branchId, String code,Long whouseId);
}
