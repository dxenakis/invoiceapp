package com.invoiceapp.shipkind;

import com.invoiceapp.shipkind.dto.ShipKindRequest;
import com.invoiceapp.shipkind.dto.ShipKindResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/shipkind")
public class ShipKindController {

    private final ShipKindService service;

    public ShipKindController(ShipKindService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShipKindResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping("/by-code/{code}")
    public ResponseEntity<ShipKindResponse> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(service.getByCode(code));
    }

    @GetMapping
    public ResponseEntity<Page<ShipKindResponse>> list(
            Pageable pageable,
            @RequestParam(required = false) Boolean onlyActive
    ) {
        return ResponseEntity.ok(service.list(pageable, onlyActive));
    }

    @PostMapping
    public ResponseEntity<ShipKindResponse> create(@Valid @RequestBody ShipKindRequest req) {
        ShipKindResponse created = service.create(req);
        return ResponseEntity.created(URI.create("/shipkind/" + created.id())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShipKindResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ShipKindRequest req
    ) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
