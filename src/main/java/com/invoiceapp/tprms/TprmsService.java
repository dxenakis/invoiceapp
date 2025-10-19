package com.invoiceapp.tprms;

import com.invoiceapp.tprms.dto.TprmsCreateRequest;
import com.invoiceapp.tprms.dto.TprmsUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TprmsService {

    Tprms create(TprmsCreateRequest req);

    Page<Tprms> list(Pageable pageable, boolean onlyActive);

    Tprms get(Long id);

    Tprms update(Long id, TprmsUpdateRequest req);

    /** Soft delete: active=false (αν θες hard delete, κάνε deleteById) */
    void delete(Long id);
}
