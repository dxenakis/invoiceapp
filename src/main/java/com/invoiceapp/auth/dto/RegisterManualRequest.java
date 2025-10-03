
package com.invoiceapp.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

public record RegisterManualRequest(
    @NotBlank String username,
    @NotBlank String password,
    @Email @NotBlank String email,

    // Company
    @NotBlank String afm,
    @NotBlank String companyName,
    String addressLine,
    String city,
    String postalCode,
    String countryCode,
    String phone,
    String companyEmail
) {}
