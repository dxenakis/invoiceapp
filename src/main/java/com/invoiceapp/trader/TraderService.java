package com.invoiceapp.trader;

import com.invoiceapp.global.TraderDomain;
import com.invoiceapp.trader.dto.TraderRequestDto;
import com.invoiceapp.trader.dto.TraderResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TraderService {

    Page<TraderResponseDto> listTraders(TraderDomain domain, String search, Pageable pageable);

    TraderResponseDto getTrader(Long id, TraderDomain domain);

    TraderResponseDto createTrader(TraderDomain domain, TraderRequestDto request);

    TraderResponseDto updateTrader(Long id, TraderDomain domain, TraderRequestDto request);

    void deleteTrader(Long id, TraderDomain domain);
}
