package com.invoiceapp.tprms;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TprmsRepository extends JpaRepository<Tprms, Long> {
    boolean existsByCode(String code);
    Tprms findByCode(String code);
    Page<Tprms> findAllByActiveTrue(Pageable pageable);
}
