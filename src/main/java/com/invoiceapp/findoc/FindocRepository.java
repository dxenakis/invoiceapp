package com.invoiceapp.findoc;

import com.invoiceapp.findoc.enums.DocumentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FindocRepository extends JpaRepository<Findoc, Long> {

    Page<Findoc> findAllByStatus(DocumentStatus status, Pageable pageable);
}
