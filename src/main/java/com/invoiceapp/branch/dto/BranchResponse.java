package com.invoiceapp.branch.dto;

import java.time.LocalDateTime;

public record BranchResponse(
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
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
