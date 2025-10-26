package com.invoiceapp.iteprms.dto;

import com.invoiceapp.global.DocumentDomain;
import com.invoiceapp.global.Effect;

public record ItePrmsUpdateRequest (
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
