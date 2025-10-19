package com.invoiceapp.seriescounter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;

import java.util.Optional;

public interface SeriesYearCounterRepository extends JpaRepository<SeriesYearCounter, Long> {

    Optional<SeriesYearCounter> findBySeriesIdAndFiscalYear(Long seriesId, Integer fiscalYear);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<SeriesYearCounter> findWithLockBySeriesIdAndFiscalYear(Long seriesId, Integer fiscalYear);
}
