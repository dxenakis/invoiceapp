package com.invoiceapp.iteprms.dto;

import com.invoiceapp.global.DocumentDomain;
import com.invoiceapp.global.Effect;

public record ItePrmsUpdateRequest (
    String description,
    DocumentDomain domain,
    Effect impqty,
    Effect impval,
    Effect expqty,
    Effect expval,
    Effect purqty,
    Effect purval,
    Effect salqty,
    Effect salval,
    Boolean active
) {}
