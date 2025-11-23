package com.invoiceapp.findoc.dto;

import java.time.LocalDate;

/**
 * Request για δημιουργία draft παραστατικού (αγοράς/πώλησης κτλ).
 *
 * documentDomain: κωδικός από το enum DocumentDomain (π.χ. 1351 για Πωλήσεις, 1251 για Αγορές).
 */
public record FindocCreateRequest(
        Long documentTypeId,
        Long branchId,
        Long seriesId,
        Long traderId,
        LocalDate documentDate,
        Integer documentDomain
) {}
