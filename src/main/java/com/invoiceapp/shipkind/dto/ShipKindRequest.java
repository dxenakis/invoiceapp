package com.invoiceapp.shipkind.dto;

import jakarta.validation.constraints.NotBlank;

public record ShipKindRequest(
        @NotBlank String code,
        @NotBlank String name,
        String name1,
        Integer mydataShipKindCode,
        Boolean active
) {}
