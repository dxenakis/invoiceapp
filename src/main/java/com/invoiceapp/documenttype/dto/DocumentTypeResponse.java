package com.invoiceapp.documenttype.dto;

import java.time.LocalDateTime;

public record DocumentTypeResponse(
        Long id,
        Long companyId,
        String code,
        String description,
        Integer domain,
        Long tprmsId,
        String tprmsCode,
        String tprmsDescription,
        Long iteprmsId,
        String iteprmsCode,
        String iteprmsDescription,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
