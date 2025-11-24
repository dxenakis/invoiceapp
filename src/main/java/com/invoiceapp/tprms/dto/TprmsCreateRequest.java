package com.invoiceapp.tprms.dto;

public record TprmsCreateRequest(
        String code,
        String description,
        Integer domain,
        Integer debit,
        Integer credit,
        Integer turnover,
        Integer sign,
        Boolean active
) {}
