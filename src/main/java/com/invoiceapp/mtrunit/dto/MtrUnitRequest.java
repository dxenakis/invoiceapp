package com.invoiceapp.mtrunit.dto;

import jakarta.validation.constraints.NotBlank;

public record MtrUnitRequest(
        @NotBlank String code,
        @NotBlank String name,
        String name1,
        String isoCode,
        Integer mydataCode,
        Boolean active
) {}
