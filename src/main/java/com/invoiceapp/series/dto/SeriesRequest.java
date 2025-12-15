package com.invoiceapp.series.dto;

import jakarta.validation.constraints.NotNull;

public record SeriesRequest(
        @NotNull Long documentTypeId,
        @NotNull Integer domain,
        @NotNull Long branchId,
        @NotNull Long whouseId,
        String code,
        String description,
        Boolean active,
        String prefix,
        String formatPattern,
        Integer paddingLength
) {}
