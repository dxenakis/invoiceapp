package com.invoiceapp.series.dto;

public record SeriesResponse(
        Long id,
        Long companyId,
        Long documentTypeId,
        Long branchId,
        String code,
        String description,
        boolean active,
        String prefix,
        String formatPattern,
        Integer paddingLength
) {}
