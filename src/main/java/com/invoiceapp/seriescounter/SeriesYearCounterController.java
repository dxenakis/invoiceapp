package com.invoiceapp.seriescounter;

import com.invoiceapp.companyscope.RequireTenant;
import com.invoiceapp.seriescounter.dto.NextNumberRequest;
import com.invoiceapp.seriescounter.dto.NextNumberResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/series-counters")
@RequireTenant
public class SeriesYearCounterController {

    private final SeriesYearCounterService service;

    public SeriesYearCounterController(SeriesYearCounterService service) {
        this.service = service;
    }

    @PostMapping("/next-number")
    public ResponseEntity<NextNumberResponse> nextNumber(@RequestBody NextNumberRequest req) {
        return ResponseEntity.ok(service.nextNumber(req));
    }

    @GetMapping("/by-series/{seriesId}")
    public ResponseEntity<Page<SeriesYearCounter>> listBySeries(@PathVariable Long seriesId, Pageable pageable) {
        return ResponseEntity.ok(service.listBySeries(seriesId, pageable));
    }
}
