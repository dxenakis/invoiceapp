package com.invoiceapp.findoc.dto;

import java.math.BigDecimal;

public record MtrLineRequest(
        Long mtrlId,
        Long vatId,
        Integer lineNo,
        BigDecimal qty,
        BigDecimal price,
        BigDecimal discountRate
) {}
