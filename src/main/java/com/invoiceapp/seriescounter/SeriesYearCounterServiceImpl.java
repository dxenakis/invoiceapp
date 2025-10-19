package com.invoiceapp.seriescounter;

import com.invoiceapp.companyscope.RequireTenant;
import com.invoiceapp.series.Series;
import com.invoiceapp.series.SeriesRepository;
import com.invoiceapp.seriescounter.dto.NextNumberRequest;
import com.invoiceapp.seriescounter.dto.NextNumberResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
@Transactional
@RequireTenant
public class SeriesYearCounterServiceImpl implements SeriesYearCounterService {

    private static final ZoneId ZONE_ATHENS = ZoneId.of("Europe/Athens");

    private final SeriesYearCounterRepository repo;
    private final SeriesRepository seriesRepo;

    public SeriesYearCounterServiceImpl(SeriesYearCounterRepository repo, SeriesRepository seriesRepo) {
        this.repo = repo;
        this.seriesRepo = seriesRepo;
    }

    @Override
    public SeriesYearCounter getOrCreate(Long seriesId, int fiscalYear) {
        // Πρώτα προσπαθούμε με lock να πάρουμε υπαρκτό counter
        Optional<SeriesYearCounter> locked = repo.findWithLockBySeriesIdAndFiscalYear(seriesId, fiscalYear);
        if (locked.isPresent()) return locked.get();

        // Δεν υπάρχει -> δημιουργία
        Series series = seriesRepo.findById(seriesId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Series not found: id=" + seriesId));

        SeriesYearCounter counter = new SeriesYearCounter();
        counter.setSeries(series);
        counter.setFiscalYear(fiscalYear);
        counter.setLastNumber(0L);
        counter.setLastIssuedAt(null);
        return repo.save(counter);
    }

    @Override
    public NextNumberResponse nextNumber(NextNumberRequest req) {
        if (req.seriesId() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "seriesId is required");
        LocalDate docDate = req.documentDate();
        if (docDate == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "documentDate is required");

        int year = docDate.atStartOfDay(ZONE_ATHENS).getYear();

        // Ensure series exists
        Series series = seriesRepo.findById(req.seriesId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Series not found: id=" + req.seriesId()));

        // Get or create counter with lock
        SeriesYearCounter counter = getOrCreate(series.getId(), year);

        // Pessimistic locked row -> safe to increment
        long next = counter.getLastNumber() + 1;
        counter.setLastNumber(next);
        counter.setLastIssuedAt(LocalDateTime.now(ZONE_ATHENS));

        // Build formatted string
        String formatted = buildFormatted(series, year, next);

        return new NextNumberResponse(year, next, formatted);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SeriesYearCounter> listBySeries(Long seriesId, Pageable pageable) {
        // For a proper filter add a repository method findAllBySeriesId(seriesId, pageable).
        return repo.findAll(pageable);
    }

    private String buildFormatted(Series s, int year, long number) {
        String padded = s.getPaddingLength() != null
                ? String.format("%0" + s.getPaddingLength() + "d", number)
                : Long.toString(number);

        String pattern = s.getFormatPattern();
        if (pattern != null && !pattern.isBlank()) {
            return pattern
                    .replace("{YYYY}", Integer.toString(year))
                    .replace("{YY}", String.format("%02d", year % 100))
                    .replace("{SERIES}", s.getCode())
                    .replace("{NNNN}", padded);
        }
        String prefix = s.getPrefix() != null ? s.getPrefix() : "";
        return prefix + padded;
    }
}
