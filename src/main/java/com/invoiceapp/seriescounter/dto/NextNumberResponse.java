package com.invoiceapp.seriescounter.dto;

public record NextNumberResponse(
        int fiscalYear,
        long number,
        String formatted // προαιρετικά μορφοποιημένο string
) {}
