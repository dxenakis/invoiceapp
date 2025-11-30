package com.invoiceapp.shipkind;

import com.invoiceapp.shipkind.dto.ShipKindRequest;
import com.invoiceapp.shipkind.dto.ShipKindResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShipKindService {

    ShipKindResponse create(ShipKindRequest req);

    ShipKindResponse get(Long id);

    ShipKindResponse getByCode(String code);

    Page<ShipKindResponse> list(Pageable pageable, Boolean onlyActive);

    ShipKindResponse update(Long id, ShipKindRequest req);

    void delete(Long id);
}
