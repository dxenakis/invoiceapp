package com.invoiceapp.findoc.mtrdoc.dto;

public record MtrdocRequest(
        Long findocId,        // σε ποιο παραστατικό ανήκει
        String addressLine1,
        String addressLine2,
        String city,
        String region,
        String postalCode,
        String countryCode,
        Long whouseId         // αποθήκη που συνδέεται με την παράδοση
) {}
