package com.invoiceapp.company.dto;

import com.invoiceapp.company.Company;

public record CompanyResponse(
    Long id,
    String afm,
    String name,
    String addressLine,
    String city,
    String postalCode,
    String countryCode,
    String email,
    String phone
) {
    public static CompanyResponse fromEntity(Company c) {
        return new CompanyResponse(
            c.getId(),
            c.getAfm(),
            c.getName(),
            c.getAddressLine(),
            c.getCity(),
            c.getPostalCode(),
            c.getCountryCode(),
            c.getEmail(),
            c.getPhone()
        );
    }
}
