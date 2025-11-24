package com.invoiceapp.iteprms.dto;


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
