// src/main/java/com/invoiceapp/payment/dto/PaymentMethodResponse.java
package com.invoiceapp.payment.dto;

public record PaymentMethodResponse(
        Long id,
        Long companyId,
        String code,
        String description,
        Integer mydataMethodCode, // μόνο code — το label θα το πάρει το frontend από lookup
        boolean active
) {}
