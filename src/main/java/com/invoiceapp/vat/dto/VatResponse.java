package com.invoiceapp.vat.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public record VatResponse(
        Long id,
        String code,
        String description,
        BigDecimal rate,
        Integer mydataVatCode,
        String mydataVatLabel,
        Integer mydataVatPercent,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
