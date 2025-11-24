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
        Long iteprmsId,
        String iteprmsCode,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
