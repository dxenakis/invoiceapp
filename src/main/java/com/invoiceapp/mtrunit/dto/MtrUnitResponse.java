package com.invoiceapp.mtrunit.dto;

import java.time.LocalDateTime;

public record MtrUnitResponse(
        Long id,
        String code,
        String name,
        String name1,
        String isoCode,
        Integer mydataCode,
        String mydataLabel,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
