package com.invoiceapp.shipkind.dto;

import java.time.LocalDateTime;

public record ShipKindResponse(
        Long id,
        String code,
        String name,
        String name1,
        Integer mydataShipKindCode,
        String mydataShipKindLabel,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
