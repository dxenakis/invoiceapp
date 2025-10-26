package com.invoiceapp.customer.dto;

import com.invoiceapp.company.Company;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CustomerRequest(
        @NotBlank String code,
        @NotBlank String name,
        String phone,
        String address,
        String city,
        String zip,
        @NotNull Long countryId
) {}
