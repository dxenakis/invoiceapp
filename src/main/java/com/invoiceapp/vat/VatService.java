package com.invoiceapp.vat;

import com.invoiceapp.vat.dto.VatRequest;
import com.invoiceapp.vat.dto.VatResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VatService {
    VatResponse create(VatRequest req);
    VatResponse get(Long id);
    VatResponse getByCode(String code);
    Page<VatResponse> list(Pageable pageable, Boolean onlyActive);
    VatResponse update(Long id, VatRequest req);
    void delete(Long id);
}
