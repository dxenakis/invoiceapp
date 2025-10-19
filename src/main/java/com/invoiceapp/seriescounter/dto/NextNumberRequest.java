package com.invoiceapp.seriescounter.dto;

import java.time.LocalDate;

/** Αίτημα για απόδοση επόμενου αριθμού */
public record NextNumberRequest(
        Long seriesId,
        LocalDate documentDate // η ημερομηνία του παραστατικού
) {}
