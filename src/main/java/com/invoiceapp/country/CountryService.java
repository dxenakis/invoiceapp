package com.invoiceapp.country;

import com.invoiceapp.country.dto.CountryRequest;
import com.invoiceapp.country.dto.CountryResponse;

public interface CountryService {

    CountryResponse getById(Long id);

    CountryResponse getByIsoCode(String isoCode);

    CountryResponse create(CountryRequest request);

    CountryResponse update(Long id, CountryRequest request);

    void delete(Long id);
}
