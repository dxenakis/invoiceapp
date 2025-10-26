// src/main/java/com/invoiceapp/payment/PaymentMethodRepository.java
package com.invoiceapp.payment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    Optional<PaymentMethod> findByCode(String code);                // unique per tenant
    Page<PaymentMethod> findAllByActiveTrue(Pageable pageable);
}
