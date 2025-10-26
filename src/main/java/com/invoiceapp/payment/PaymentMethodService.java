// src/main/java/com/invoiceapp/payment/PaymentMethodService.java
package com.invoiceapp.payment;

import com.invoiceapp.payment.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentMethodService {
    PaymentMethodResponse create(PaymentMethodCreateRequest req);
    PaymentMethodResponse update(Long id, PaymentMethodUpdateRequest req);
    void delete(Long id);
    PaymentMethodResponse get(Long id);
    Page<PaymentMethodResponse> list(Pageable pageable, Boolean activeOnly);
    void seedDefaultsIfEmpty(); // για αρχικοποίηση tenant
}
