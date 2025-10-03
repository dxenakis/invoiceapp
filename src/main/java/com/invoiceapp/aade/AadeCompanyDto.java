
package com.invoiceapp.aade;

public record AadeCompanyDto(
        String afm,
        String companyName,
        String addressLine,
        String city,
        String postalCode,
        String countryCode,
        String companyEmail,
        String phone
) {}
