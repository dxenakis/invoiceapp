package com.invoiceapp.findoc.dto;

import com.invoiceapp.findoc.mtrdoc.dto.MtrdocRequest;
import com.invoiceapp.findoc.mtrlines.dto.MtrLineRequest;

import java.time.LocalDate;
import java.util.List;

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
        Integer documentDomain,
        Long paymentMethodId,   // νέο
        Long shipKindId         // νέο
) {}

