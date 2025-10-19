package com.invoiceapp.seriescounter;

import com.invoiceapp.seriescounter.dto.NextNumberRequest;
import com.invoiceapp.seriescounter.dto.NextNumberResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SeriesYearCounterService {

    /** Επιστροφή/δημιουργία counter για συγκεκριμένη σειρά/έτος */
    SeriesYearCounter getOrCreate(Long seriesId, int fiscalYear);

    /** Απόδοση επόμενου αριθμού με βάση την ημερομηνία παραστατικού (auto reset ανά έτος) */
    NextNumberResponse nextNumber(NextNumberRequest req);

    /** Λίστα counters για μια σειρά */
    Page<SeriesYearCounter> listBySeries(Long seriesId, Pageable pageable);
}
