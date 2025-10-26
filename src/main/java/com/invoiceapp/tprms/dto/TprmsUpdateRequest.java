package com.invoiceapp.tprms.dto;

import com.invoiceapp.global.DocumentDomain;
import com.invoiceapp.global.Effect;
import com.invoiceapp.global.Sign;

public record TprmsUpdateRequest(
        String description,
        Integer domain,
        Integer debit,
        Integer credit,
        Integer turnover,
        Integer sign,
        Boolean active
) {}
