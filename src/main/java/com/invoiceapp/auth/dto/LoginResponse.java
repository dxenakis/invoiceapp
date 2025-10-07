// com.invoiceapp.auth.dto.LoginResponse
package com.invoiceapp.auth.dto;

import com.invoiceapp.user.Role;

import java.util.List;

public record LoginResponse(
        String token,
        Long activeCompanyId,
        List<CompanyAccessItem> companies
) {
    public record CompanyAccessItem(Long companyId, Role role, String companyName) {}
}
