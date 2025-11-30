package com.invoiceapp.findoc.mtrlines.dto;

import java.math.BigDecimal;

public record MtrLineResponse(
        Long id,
        Integer lineNo,
        Long mtrlId,
        String mtrlCode,
        String mtrlName,
        Long vatId,
        BigDecimal vatRate,
        Long mtrUnitId,
        Long whouseId,
        BigDecimal qty,
        BigDecimal price,
        BigDecimal discountRate,
        BigDecimal netAmount,
        BigDecimal vatAmount,
        BigDecimal totalAmount
) {}