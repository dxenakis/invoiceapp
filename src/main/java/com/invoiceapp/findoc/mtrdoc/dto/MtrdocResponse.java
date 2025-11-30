package com.invoiceapp.findoc.mtrdoc.dto;

public record MtrdocResponse(
        Long id,
        Long findocId,
        String addressLine1,
        String addressLine2,
        String city,
        String region,
        String postalCode,
        String countryCode,
        Long whouseId,
        String whouseCode,
        String whouseName
) {}
