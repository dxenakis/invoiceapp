package com.invoiceapp.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterManualRequest(
        @NotBlank String username,
        @NotBlank String password,
        @Email @NotBlank String email
) {}
