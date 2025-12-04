package com.invoiceapp.series.dto;

public record SeriesRequest(
        Long documentTypeId,
        Long branchId,
        Long whouseId,
        String code,
        String description,
        Boolean active,
        String prefix,
        String formatPattern,
        Integer paddingLength
) {}
