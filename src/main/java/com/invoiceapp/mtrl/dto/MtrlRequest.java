package com.invoiceapp.mtrl.dto;

import com.invoiceapp.mtrl.AccountingCategory;
import jakarta.persistence.Column;

import java.time.Instant;
import java.time.LocalDateTime;

public record MtrlRequest(
        String code,
        String name,
        String name1,
        AccountingCategory accountCategory,
        float pricer,
        float pricew,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
