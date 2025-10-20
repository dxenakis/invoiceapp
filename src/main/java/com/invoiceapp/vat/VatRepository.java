package com.invoiceapp.vat;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VatRepository extends JpaRepository<Vat, Long> {
    boolean existsByCode(String code);
    Optional<Vat> findByCode(String code);
}
