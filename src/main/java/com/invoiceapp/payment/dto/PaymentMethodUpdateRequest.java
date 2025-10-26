// src/main/java/com/invoiceapp/payment/dto/PaymentMethodUpdateRequest.java
package com.invoiceapp.payment.dto;

public record PaymentMethodUpdateRequest(
        String description,
        Integer mydataMethodCode,
        Boolean active
) {}
