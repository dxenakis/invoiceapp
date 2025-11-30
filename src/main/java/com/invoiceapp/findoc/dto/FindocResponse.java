package com.invoiceapp.findoc.dto;

import com.invoiceapp.findoc.enums.DocumentDomain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import com.invoiceapp.findoc.mtrdoc.dto.MtrdocResponse;
import com.invoiceapp.findoc.mtrlines.dto.MtrLineResponse;

/**
 * Response για Findoc, μαζί με γραμμές.
 */
public record FindocResponse(
        Long id,
        Long companyId,
        Long documentTypeId,
        Long branchId,
        Long seriesId,
        DocumentDomain documentDomain,
        Integer fiscalYear,
        Long number,
        String printedNumber,
        LocalDate documentDate,
        Long traderId,
        Integer status,
        BigDecimal totalNet,
        BigDecimal totalVat,
        BigDecimal totalGross,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,

        // νέα πεδία header
        Long paymentMethodId,
        String paymentMethodCode,
        String paymentMethodDescription,
        Long shipKindId,
        String shipKindCode,
        String shipKindName,

        // στοιχεία παράδοσης
        MtrdocResponse mtrdoc,

        List<MtrLineResponse> mtrlines
) {

}
