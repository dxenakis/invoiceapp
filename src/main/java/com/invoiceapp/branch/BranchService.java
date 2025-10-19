package com.invoiceapp.branch;

import com.invoiceapp.branch.dto.BranchRequest;
import com.invoiceapp.branch.dto.BranchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BranchService {

    BranchResponse create(BranchRequest req);

    BranchResponse getById(Long id);

    BranchResponse getByCode(String code);

    Page<BranchResponse> list(Pageable pageable, Boolean onlyActive);

    /** Λίστα για συγκεκριμένη εταιρεία (ανεξάρτητα από το @TenantId, για admin σενάρια). */
    Page<BranchResponse> listByCompany(Long companyId, Pageable pageable, Boolean onlyActive);

    /** Επιστρέφει τα headquarters (αν θες να στηρίξεις μοναδικό HQ ανά εταιρεία, το χειριζόμαστε στο service). */
    List<BranchResponse> listHeadquarters(Long companyId);

    BranchResponse update(Long id, BranchRequest req);

    /** Soft delete: active=false */
    void delete(Long id);
}
