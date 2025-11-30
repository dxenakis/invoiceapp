package com.invoiceapp.whouse.dto;

import java.time.LocalDateTime;

public record WhouseResponse(
        Long id,
        Long companyId,
        String code,
        String name,
        String description,
        String addressLine1,
        String addressLine2,
        String city,
        String region,
        String postalCode,
        String countryCode,
        String phone,
        String email,
        boolean headquarters,
        boolean active,
        Long branchId,
        String branchCode,
        String branchName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
