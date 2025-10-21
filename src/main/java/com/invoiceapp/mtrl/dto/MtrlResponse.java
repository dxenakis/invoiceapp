package com.invoiceapp.mtrl.dto;

import com.invoiceapp.mtrl.AccountingCategory;

import java.time.Instant;

public record MtrlResponse(
        Long id,
        Long companyid,
        String code,
        String name,
        String name1,
        AccountingCategory accountCategory,
        float pricer,
        float pricew,
        Boolean active,
        Instant createdAt,
        Instant updatedAt) {
}