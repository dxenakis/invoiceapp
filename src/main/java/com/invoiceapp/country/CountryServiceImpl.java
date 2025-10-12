package com.invoiceapp.country;

import com.invoiceapp.country.dto.CountryRequest;
import com.invoiceapp.country.dto.CountryResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional // write-by-default; read-only methods override below
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    // ===== Helpers (mapping & normalization) =====
    private static CountryResponse toDto(Country c) {
        return new CountryResponse(
                c.getId(),
                c.getIsoCode(),
                c.getName()
        );
    }

    private static String normalizeIso(String iso) {
        return StringUtils.hasText(iso) ? iso.trim().toUpperCase() : iso;
    }

    private static void applyRequestToEntity(CountryRequest req, Country entity) {
        entity.setName(req.name());
        entity.setIsoCode(normalizeIso(req.isoCode()));
    }

    private Country getOr404(Long id) {
        return countryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Country not found"));
    }

    // ===== Service methods =====
    @Override
    @Transactional(readOnly = true)
    public CountryResponse getById(Long id) {
        return toDto(getOr404(id));
    }

    @Override
    @Transactional(readOnly = true)
    public CountryResponse getByIsoCode(String isoCode) {
        return countryRepository.findByIsoCode(normalizeIso(isoCode))
                .map(CountryServiceImpl::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Country not found"));
    }

    @Override
    public CountryResponse create(CountryRequest request) {
        Country entity = new Country();
        applyRequestToEntity(request, entity);
        Country saved = countryRepository.save(entity);
        return toDto(saved);
    }

    @Override
    public CountryResponse update(Long id, CountryRequest request) {
        Country existing = getOr404(id);
        applyRequestToEntity(request, existing);
        Country saved = countryRepository.save(existing);
        return toDto(saved);
    }

    @Override
    public void delete(Long id) {
        Country existing = getOr404(id);
        countryRepository.delete(existing);
    }
}
