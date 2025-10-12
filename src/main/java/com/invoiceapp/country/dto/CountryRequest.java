package com.invoiceapp.country.dto;

import jakarta.validation.constraints.NotBlank;

public record CountryRequest(
                                       Long id,
                             @NotBlank String name,
                             @NotBlank String isoCode) {
}
