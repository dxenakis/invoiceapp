package com.invoiceapp.iteprms;

import com.invoiceapp.findoc.enums.DocumentDomain;
import com.invoiceapp.iteprms.dto.ItePrmsCreateRequest;
import com.invoiceapp.iteprms.dto.ItePrmsUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItePrmsService {

    ItePrms create(ItePrmsCreateRequest req);
    Page<ItePrms> list(Pageable pageable, boolean onlyActive);
    ItePrms get(Long id);
    ItePrms update(Long id, ItePrmsUpdateRequest req);
    List<ItePrms> findAllByDomain (DocumentDomain documentDomain);
    /** Soft delete: active=false (αν θες hard delete, κάνε deleteById) */
    void delete(Long id);
}
