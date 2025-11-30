package com.invoiceapp.findoc;

import com.invoiceapp.findoc.dto.FindocCreateRequest;
import com.invoiceapp.findoc.dto.FindocResponse;
import com.invoiceapp.findoc.mtrlines.dto.MtrLineRequest;
import com.invoiceapp.findoc.enums.DocumentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.invoiceapp.findoc.mtrdoc.dto.MtrdocRequest;


public interface FindocService {

    FindocResponse createDraft(FindocCreateRequest req);

    FindocResponse upsertLine(Long findocId, MtrLineRequest line);

    FindocResponse deleteLine(Long findocId, Long lineId);

    FindocResponse post(Long findocId);

    FindocResponse cancel(Long findocId);

    FindocResponse get(Long id);

    FindocResponse updateMtrdoc(Long findocId, MtrdocRequest request);

    Page<FindocResponse> list(Pageable pageable, DocumentStatus status);
}
