package com.invoiceapp.country.dto;

import jakarta.validation.constraints.NotBlank;

public record CountryResponse(Long id,
                              String isoCode,
                              String name) {
}
