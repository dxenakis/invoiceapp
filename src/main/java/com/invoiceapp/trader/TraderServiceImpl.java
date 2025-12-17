package com.invoiceapp.trader;

import com.invoiceapp.country.Country;
import com.invoiceapp.country.CountryRepository;
import com.invoiceapp.global.enums.TraderDomain;
import com.invoiceapp.trader.dto.TraderRequestDto;
import com.invoiceapp.trader.dto.TraderResponseDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@Transactional
public class TraderServiceImpl implements TraderService {

    private final TraderRepository traderRepository;
    private final CountryRepository countryRepository;

    public TraderServiceImpl(TraderRepository traderRepository,
                             CountryRepository countryRepository) {
        this.traderRepository = traderRepository;
        this.countryRepository = countryRepository;
    }

    // --------- Helpers ---------

    private Country getCountryOrThrow(Long countryId) {
        return countryRepository.findById(countryId)
                .orElseThrow(() -> new EntityNotFoundException("Country not found id=" + countryId));
    }

    private Trader getByIdAndDomainOrThrow(Long id, TraderDomain domain) {
        return traderRepository.findByIdAndTraderDomain(id, domain)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Trader not found id=" + id + " domain=" + domain));
    }

    private void applyRequestToEntity(TraderRequestDto request, Trader trader, Country country) {
        trader.setCode(request.getCode());
        trader.setName(request.getName());
        trader.setEmail(request.getEmail());
        trader.setPhone(request.getPhone());
        trader.setCellphone(request.getCellphone());
        trader.setAddress(request.getAddress());
        trader.setCity(request.getCity());
        trader.setZip(request.getZip());
        trader.setCountry(country);
    }

    //-------- Implementation ---------

    @Override
    @Transactional(readOnly = true)
    public Page<TraderResponseDto> listTraders(TraderDomain domain, String search, Pageable pageable) {
        Page<Trader> page;

        if (search == null || search.isBlank()) {
            page = traderRepository.findByTraderDomain(domain, pageable);
        } else {
            String pattern = "%" + search.trim().toLowerCase(Locale.ROOT) + "%";
            page = traderRepository.search(domain, pattern, pageable);

        }

        return page.map(TraderResponseDto::fromEntity);
    }





    @Override
    @Transactional(readOnly = true)
    public TraderResponseDto getTrader(Long id, TraderDomain domain) {
        Trader trader = getByIdAndDomainOrThrow(id, domain);
        return TraderResponseDto.fromEntity(trader);
    }

    @Override
    public TraderResponseDto createTrader(TraderDomain domain, TraderRequestDto request) {
        Country country = getCountryOrThrow(request.getCountryId());

        Trader trader = new Trader();
        trader.setTraderDomain(domain);
        applyRequestToEntity(request, trader, country);

        Trader saved = traderRepository.save(trader);
        return TraderResponseDto.fromEntity(saved);
    }

    @Override
    public TraderResponseDto updateTrader(Long id, TraderDomain domain, TraderRequestDto request) {
        Trader trader = getByIdAndDomainOrThrow(id, domain);
        Country country = getCountryOrThrow(request.getCountryId());

        applyRequestToEntity(request, trader, country);

        Trader saved = traderRepository.save(trader);
        return TraderResponseDto.fromEntity(saved);
    }

    @Override
    public void deleteTrader(Long id, TraderDomain domain) {
        Trader trader = getByIdAndDomainOrThrow(id, domain);
        traderRepository.delete(trader);
    }
}
