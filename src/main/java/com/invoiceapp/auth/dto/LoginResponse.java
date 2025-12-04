// com.invoiceapp.auth.dto.LoginResponse
package com.invoiceapp.auth.dto;

import com.invoiceapp.user.GlobalRole;
import com.invoiceapp.user.Role;

import java.util.List;

public record LoginResponse(
        String token,
        GlobalRole globalRole,
        Long activeCompanyId,
        String username,
        String firstname,
        String lastname,
        List<CompanyAccessItem> companies
) {
    public record CompanyAccessItem(Long companyId, Role role, String companyName,String Vat) {}
}
