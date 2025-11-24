package com.invoiceapp.documenttype;

import com.invoiceapp.findoc.enums.DocumentDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long> {

    boolean existsByCode(String code); // μοναδικότητα σε επίπεδο tenant

    Optional<DocumentType> findByCode(String code);

    Page<DocumentType> findAllByDomain(DocumentDomain domain, Pageable pageable);
}
