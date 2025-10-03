
package com.invoiceapp.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

public record RegisterGovRequest(
    @NotBlank String username,
    @NotBlank String password,
    @Email @NotBlank String email,
    @NotBlank String govAuthCode
) {}
