package com.invoiceapp.iteprms.dto;

import com.invoiceapp.global.DocumentDomain;
import com.invoiceapp.global.Effect;
import com.invoiceapp.global.Sign;


public record ItePrmsCreateRequest(
        String code,
        String description,
        Integer domain,
        Integer impqty,
        Integer impval,
        Integer expqty,
        Integer expval,
        Integer purqty,
        Integer purval,
        Integer salqty,
        Integer salval,
        Boolean active
) {}
