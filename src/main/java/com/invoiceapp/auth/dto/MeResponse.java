package com.invoiceapp.auth.dto;

import com.invoiceapp.user.Role;

import java.util.List;


public record MeResponse(

        String username,
        String firstname,
        String lastname,
        List<CompanyAccessItem> companies
) {
    public record CompanyAccessItem(Long companyId, Role role, String companyName, String Vat) {}
}
