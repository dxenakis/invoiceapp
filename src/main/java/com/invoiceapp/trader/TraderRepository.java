package com.invoiceapp.trader;

import com.invoiceapp.global.enums.TraderDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TraderRepository extends JpaRepository<Trader, Long> {

    Page<Trader> findByTraderDomain(TraderDomain domain, Pageable pageable);

    Page<Trader> findByTraderDomainAndNameContainingIgnoreCase(
            TraderDomain domain,
            String name,
            Pageable pageable
    );

    Optional<Trader> findByIdAndTraderDomain(Long id, TraderDomain domain);
}
