package com.invoiceapp.tprms.dto;

import com.invoiceapp.global.DocumentDomain;
import com.invoiceapp.global.Effect;
import com.invoiceapp.global.Sign;

public record TprmsUpdateRequest(
        String description,
        DocumentDomain domain,
        Effect debit,
        Effect credit,
        Effect turnover,
        Sign sign,
        Boolean active
) {}
