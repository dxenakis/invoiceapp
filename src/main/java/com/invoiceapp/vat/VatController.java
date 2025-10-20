package com.invoiceapp.vat;

import com.invoiceapp.vat.dto.VatRequest;
import com.invoiceapp.vat.dto.VatResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/vat")
public class VatController {

    private final VatService service;

    public VatController(VatService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<Page<VatResponse>> list(Pageable pageable,
                                                  @RequestParam(required = false) Boolean onlyActive) {
        return ResponseEntity.ok(service.list(pageable, onlyActive));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VatResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<VatResponse> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(service.getByCode(code));
    }

    @PostMapping
    public ResponseEntity<VatResponse> create(@Valid @RequestBody VatRequest req) {
        VatResponse created = service.create(req);
        return ResponseEntity.created(URI.create("/vat/" + created.id())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VatResponse> update(@PathVariable Long id, @Valid @RequestBody VatRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
