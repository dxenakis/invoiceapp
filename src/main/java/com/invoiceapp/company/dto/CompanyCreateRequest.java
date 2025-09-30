package com.invoiceapp.company.dto;

import jakarta.validation.constraints.NotBlank;

public record CompanyCreateRequest(
    @NotBlank String afm,
    @NotBlank String name,
    String addressLine,
    String city,
    String postalCode,
    String countryCode,
    String email,
    String phone
) {}
