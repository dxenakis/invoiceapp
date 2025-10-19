package com.invoiceapp.branch.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record BranchRequest(
        @NotBlank String code,
        @NotBlank String name,
        String description,
        String addressLine1,
        String addressLine2,
        String city,
        String region,
        String postalCode,
        String countryCode, // ISO-2
        String phone,
        @Email String email,
        Boolean headquarters, // nullable => αν δεν σταλεί, δεν αλλάζει
        Boolean active        // nullable
) {}
