package com.invoiceapp.mtrunit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MtrUnitRepository extends JpaRepository<MtrUnit, Long> {
    boolean existsByCode(String code);
    Optional<MtrUnit> findByCode(String code);
    Page<MtrUnit> findByActiveTrue(Pageable pageable);
}
