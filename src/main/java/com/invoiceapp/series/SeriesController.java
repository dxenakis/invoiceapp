package com.invoiceapp.series;

import com.invoiceapp.series.dto.SeriesRequest;
import com.invoiceapp.series.dto.SeriesResponse;
import com.invoiceapp.companyscope.RequireTenant;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/series")
@RequireTenant
public class SeriesController {

    private final SeriesService service;

    public SeriesController(SeriesService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<SeriesResponse>> list(Pageable pageable) {
        return ResponseEntity.ok(service.list(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeriesResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<SeriesResponse> create(@Valid @RequestBody SeriesRequest req) {
        SeriesResponse created = service.create(req);
        return ResponseEntity.created(URI.create("/series/" + created.id())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeriesResponse> update(@PathVariable Long id, @Valid @RequestBody SeriesRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
