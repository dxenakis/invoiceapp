package com.invoiceapp.mtrl.dto;

import com.invoiceapp.mtrl.AccountingCategory;
import jakarta.persistence.Column;

import java.time.Instant;

public record MtrlRequest(
        String code,
        String name,
        String name1,
        AccountingCategory accountCategory,
        float pricer,
        float pricew,
        Boolean active,
        Instant createdAt,
        Instant updatedAt
) {
}
