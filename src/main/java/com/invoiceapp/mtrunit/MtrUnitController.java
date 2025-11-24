package com.invoiceapp.mtrunit;

import com.invoiceapp.mtrunit.dto.MtrUnitRequest;
import com.invoiceapp.mtrunit.dto.MtrUnitResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/mtrunit")
public class MtrUnitController {

    private final MtrUnitService service;

    public MtrUnitController(MtrUnitService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<MtrUnitResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping("/by-code/{code}")
    public ResponseEntity<MtrUnitResponse> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(service.getByCode(code));
    }

    @GetMapping
    public ResponseEntity<Page<MtrUnitResponse>> list(
            Pageable pageable,
            @RequestParam(required = false) Boolean onlyActive
    ) {
        return ResponseEntity.ok(service.list(pageable, onlyActive));
    }

    @PostMapping
    public ResponseEntity<MtrUnitResponse> create(@Valid @RequestBody MtrUnitRequest req) {
        MtrUnitResponse created = service.create(req);
        return ResponseEntity.created(URI.create("/mtrunit/" + created.id())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MtrUnitResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody MtrUnitRequest req
    ) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
