package com.invoiceapp.shipkind;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShipKindRepository extends JpaRepository<ShipKind, Long> {

    boolean existsByCode(String code);

    Optional<ShipKind> findByCode(String code);

    Page<ShipKind> findByActiveTrue(Pageable pageable);
}
