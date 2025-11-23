package com.invoiceapp.findoc.dto;

import com.invoiceapp.global.DocumentDomain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Response για Findoc, μαζί με γραμμές.
 */
public record FindocResponse(
        Long id,
        Long companyId,
        Long documentTypeId,
        Long branchId,
        Long seriesId,
        DocumentDomain documentDomain,
        Integer fiscalYear,
        Long number,
        String printedNumber,
        LocalDate documentDate,
        Long traderId,
        Integer status,          // κωδικός DocumentStatus (π.χ. 0=draft, 1=posted κτλ)
        BigDecimal totalNet,
        BigDecimal totalVat,
        BigDecimal totalGross,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<LineResponse> lines
) {
    public record LineResponse(
            Long id,
            Integer lineNo,
            Long mtrlId,
            String mtrlCode,
            String mtrlName,
            Long vatId,
            BigDecimal vatRate,
            BigDecimal qty,
            BigDecimal price,
            BigDecimal discountRate,
            BigDecimal netAmount,
            BigDecimal vatAmount,
            BigDecimal totalAmount
    ) {}
}
