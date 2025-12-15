package com.invoiceapp.findoc.dto;

import com.invoiceapp.findoc.mtrdoc.dto.MtrdocRequest;
import com.invoiceapp.findoc.mtrlines.dto.MtrLineRequest;

import java.time.LocalDate;
import java.util.List;

public record FindocSaveRequest(
        Long id,

        Long documentTypeId,
        Long branchId,
        Long seriesId,
        Long traderId,
        LocalDate documentDate,
        Integer documentDomain,
        Long paymentMethodId,
        Long shipKindId,

        MtrdocRequest mtrdoc,
        List<MtrLineRequest> mtrlines
) {}
