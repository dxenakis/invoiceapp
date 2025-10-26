// src/main/java/com/invoiceapp/payment/PaymentMethodController.java
package com.invoiceapp.payment;

import com.invoiceapp.companyscope.RequireTenant;
import com.invoiceapp.payment.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment-methods")
@RequireTenant
public class PaymentMethodController {

    private final PaymentMethodService svc;
    public PaymentMethodController(PaymentMethodService svc) { this.svc = svc; }

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public PaymentMethodResponse create(@RequestBody PaymentMethodCreateRequest req) { return svc.create(req); }

    @PutMapping("/{id}")
    public PaymentMethodResponse update(@PathVariable Long id, @RequestBody PaymentMethodUpdateRequest req) { return svc.update(id, req); }

    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { svc.delete(id); }

    @GetMapping("/{id}")
    public PaymentMethodResponse get(@PathVariable Long id) { return svc.get(id); }

    @GetMapping
    public Page<PaymentMethodResponse> list(Pageable pageable, @RequestParam(required = false) Boolean activeOnly) {
        return svc.list(pageable, activeOnly);
    }

    // optional: seed defaults για το ενεργό tenant
    @PostMapping("/seed-defaults") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void seedDefaults() { svc.seedDefaultsIfEmpty(); }
}
