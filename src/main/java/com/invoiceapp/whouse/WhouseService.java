package com.invoiceapp.whouse;

import com.invoiceapp.whouse.dto.WhouseRequest;
import com.invoiceapp.whouse.dto.WhouseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WhouseService {

    WhouseResponse create(WhouseRequest req);

    WhouseResponse getById(Long id);

    Page<WhouseResponse> list(Pageable pageable, Boolean onlyActive, Long branchId);

    /** Admin: λίστα αποθηκών για συγκεκριμένη εταιρεία (και προαιρετικά branch). */
    Page<WhouseResponse> listByCompany(Long companyId, Pageable pageable, Boolean onlyActive, Long branchId);
    Page<WhouseResponse> listByBranch(Long branchId, Pageable pageable, Boolean onlyActive);

    WhouseResponse update(Long id, WhouseRequest req);

    /** Soft delete: active=false */
    void delete(Long id);
}
