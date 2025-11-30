package com.invoiceapp.findoc.mtrlines.dto;

import java.math.BigDecimal;

public record MtrLineRequest(
        Long mtrlId,
        Long vatId,
        Long mtrUnitId,   // νέο
        Long whouseId,    // νέο
        Integer lineNo,
        BigDecimal qty,
        BigDecimal price,
        BigDecimal discountRate
) {}
