package com.invoiceapp.findoc.dto;

import com.invoiceapp.global.DocumentDomain;
import java.time.LocalDate;

public record FindocCreateRequest(
        Long documentTypeId,
        Long branchId,
        Long seriesId,
        Long customerId,
        LocalDate documentDate,
        Integer documentDomain
) {}
