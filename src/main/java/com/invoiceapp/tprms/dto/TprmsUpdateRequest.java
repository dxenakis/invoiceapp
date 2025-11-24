package com.invoiceapp.tprms.dto;

public record TprmsUpdateRequest(
        String description,
        Integer domain,
        Integer debit,
        Integer credit,
        Integer turnover,
        Integer sign,
        Boolean active
) {}
