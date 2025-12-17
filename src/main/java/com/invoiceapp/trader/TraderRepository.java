package com.invoiceapp.trader;

import com.invoiceapp.global.enums.TraderDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TraderRepository extends JpaRepository<Trader, Long> {

    Page<Trader> findByTraderDomain(TraderDomain domain, Pageable pageable);

    Page<Trader> findByTraderDomainAndNameContainingIgnoreCase(
            TraderDomain domain,
            String name,
            Pageable pageable
    );

    Optional<Trader> findByIdAndTraderDomain(Long id, TraderDomain domain);

    @Query("""
    select t
    from Trader t
    where t.traderDomain = :domain
      and (
           t.name      like :pattern
        or t.code      like :pattern
        or coalesce(t.phone, '')     like :pattern
        or coalesce(t.cellphone, '') like :pattern
        or coalesce(t.email, '')     like :pattern
      )
""")
    Page<Trader> search(
            @Param("domain") TraderDomain domain,
            @Param("pattern") String pattern,
            Pageable pageable
    );

}
