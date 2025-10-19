package com.invoiceapp.documenttype.dto;

import com.invoiceapp.global.DocumentDomain;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/** Χρησιμοποιείται για CREATE και UPDATE. */
public record DocumentTypeRequest(
        @NotBlank String code,
        @NotBlank String description,
        @NotNull DocumentDomain domain,
        @NotNull Long tprmsId,
        Long iteprmsId // προαιρετικό
) {}
