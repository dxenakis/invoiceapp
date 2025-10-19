package com.invoiceapp.documenttype;

import com.invoiceapp.companyscope.RequireTenant;
import com.invoiceapp.documenttype.dto.DocumentTypeRequest;
import com.invoiceapp.documenttype.dto.DocumentTypeResponse;
import com.invoiceapp.global.DocumentDomain;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/documenttypes")
@RequireTenant
public class DocumentTypeController {

    private final DocumentTypeService service;

    public DocumentTypeController(DocumentTypeService service) {
        this.service = service;
    }

    // LIST (paged)
    @GetMapping
    public ResponseEntity<Page<DocumentTypeResponse>> list(Pageable pageable) {
        return ResponseEntity.ok(service.list(pageable));
    }

    // LIST by domain (paged)
    @GetMapping("/by-domain")
    public ResponseEntity<Page<DocumentTypeResponse>> listByDomain(@RequestParam DocumentDomain domain,
                                                                   Pageable pageable) {
        return ResponseEntity.ok(service.listByDomain(domain, pageable));
    }

    // GET by id
    @GetMapping("/{id}")
    public ResponseEntity<DocumentTypeResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // GET by code
    @GetMapping("/code/{code}")
    public ResponseEntity<DocumentTypeResponse> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(service.getByCode(code));
    }

    // CREATE
    @PostMapping
    public ResponseEntity<DocumentTypeResponse> create(@Valid @RequestBody DocumentTypeRequest request) {
        DocumentTypeResponse created = service.create(request);
        return ResponseEntity.created(URI.create("/documenttypes/" + created.id())).body(created);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<DocumentTypeResponse> update(@PathVariable Long id,
                                                       @Valid @RequestBody DocumentTypeRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
