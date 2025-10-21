package com.invoiceapp.mtrl;

import com.invoiceapp.mtrl.dto.MtrlRequest;
import com.invoiceapp.mtrl.dto.MtrlResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface MtrlService {
    Page<MtrlResponse> listAll(Pageable pageable);
    MtrlResponse getById(Long id);
    MtrlResponse create(MtrlRequest mtrlRequest);
    MtrlResponse update(Long id, MtrlRequest mtrlRequest);
    void delete(Long id);
}