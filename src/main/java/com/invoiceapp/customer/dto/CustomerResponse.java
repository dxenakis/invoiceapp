package com.invoiceapp.customer.dto;

import java.time.LocalDateTime;

public record CustomerResponse(
        Long id,
        Long companyId,
        String code,
        String name,
        String phone,
        String address,
        String city,
        String zip,
        Long countryId,
        String countryName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
