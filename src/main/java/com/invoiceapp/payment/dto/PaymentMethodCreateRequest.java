// src/main/java/com/invoiceapp/payment/dto/PaymentMethodCreateRequest.java
package com.invoiceapp.payment.dto;

public record PaymentMethodCreateRequest(
        String code,
        String description,
        Integer mydataMethodCode, // μόνο int code (π.χ. 3 = Μετρητά)
        Boolean active

) {}
