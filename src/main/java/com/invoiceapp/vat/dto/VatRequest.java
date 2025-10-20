package com.invoiceapp.vat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record VatRequest(
        @NotBlank String code,
        String description,
        @NotNull BigDecimal rate,
        Boolean active
) {}
