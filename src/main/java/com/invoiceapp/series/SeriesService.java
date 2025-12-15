package com.invoiceapp.series;

import com.invoiceapp.findoc.enums.DocumentDomain;
import com.invoiceapp.series.dto.SeriesRequest;
import com.invoiceapp.series.dto.SeriesResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SeriesService {

    SeriesResponse create(SeriesRequest req);

    SeriesResponse getById(Long id);

    Page<SeriesResponse> list(Pageable pageable);
    Page<SeriesResponse> listByDomain(DocumentDomain domain,Pageable pageable);
    SeriesResponse update(Long id, SeriesRequest req);

    void delete(Long id);
}
