package com.invoiceapp.series.dto;

public record SeriesRequest(
        Long documentTypeId,
        Long branchId,
        String code,
        String description,
        Boolean active,
        String prefix,
        String formatPattern,
        Integer paddingLength
) {}
