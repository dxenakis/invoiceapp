package com.invoiceapp.mtrunit;

import com.invoiceapp.mtrunit.dto.MtrUnitRequest;
import com.invoiceapp.mtrunit.dto.MtrUnitResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MtrUnitService {

    MtrUnitResponse create(MtrUnitRequest req);

    MtrUnitResponse get(Long id);

    MtrUnitResponse getByCode(String code);

    Page<MtrUnitResponse> list(Pageable pageable, Boolean onlyActive);

    MtrUnitResponse update(Long id, MtrUnitRequest req);

    void delete(Long id);
}
