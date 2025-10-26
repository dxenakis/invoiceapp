package com.invoiceapp.findoc.dto;


import com.invoiceapp.global.DocumentDomain;
import com.invoiceapp.global.DocumentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record FindocResponse(
        Long id,
        Long companyId,
        Long documentTypeId,
        Long branchId,
        Long seriesId,
        Integer documentDomain,
        Integer fiscalYear,
        Long number,
        String printedNumber,
        LocalDate documentDate,
        Long customerId,
        Integer status,
        BigDecimal totalNet,
        BigDecimal totalVat,
        BigDecimal totalGross,
        List<FindocLineResponse> lines
) {
    public record FindocLineResponse(
            Long id,
            Integer lineNo,
            Long mtrlId,
            String mtrlCode,
            String mtrlName,
            Long vatId,
            java.math.BigDecimal vatRate,
            java.math.BigDecimal qty,
            java.math.BigDecimal price,
            java.math.BigDecimal discountRate,
            java.math.BigDecimal netAmount,
            java.math.BigDecimal vatAmount,
            java.math.BigDecimal totalAmount
    ) {}
}
