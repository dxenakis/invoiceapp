package com.invoiceapp.whouse.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record WhouseRequest(
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
        Boolean headquarters, // όπως στο branch (nullable για update)
        Boolean active,       // nullable
        @NotNull Long branchId // 🔗 σε ποιο υποκατάστημα ανήκει η αποθήκη
) {}
