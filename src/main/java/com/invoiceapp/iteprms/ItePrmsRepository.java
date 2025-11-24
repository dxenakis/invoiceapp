package com.invoiceapp.iteprms;

import com.invoiceapp.findoc.enums.DocumentDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItePrmsRepository extends JpaRepository<ItePrms, Long> {
    boolean existsByCode(String code);
    ItePrms findByCode(String code);
    Page<ItePrms> findAllByActiveTrue(Pageable pageable);
    List<ItePrms> findAllByDomain(DocumentDomain domain);
}
