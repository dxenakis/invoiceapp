package com.invoiceapp.tprms;

import com.invoiceapp.companyscope.RequireTenant;
import com.invoiceapp.global.DocumentDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TprmsRepository extends JpaRepository<Tprms, Long> {
    boolean existsByCode(String code);
    Tprms findByCode(String code);
    Page<Tprms> findAllByActiveTrue(Pageable pageable);
    List<Tprms> findAllByDomain(DocumentDomain domain);
}
