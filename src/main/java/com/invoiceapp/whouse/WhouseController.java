package com.invoiceapp.whouse;

import com.invoiceapp.companyscope.RequireTenant;
import com.invoiceapp.whouse.dto.WhouseRequest;
import com.invoiceapp.whouse.dto.WhouseResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/whouses")
@RequireTenant
public class WhouseController {

    private final WhouseService service;

    public WhouseController(WhouseService service) {
        this.service = service;
    }

    // LIST (τρέχων tenant, με φίλτρο onlyActive + προαιρετικό branchId)
    @GetMapping
    public ResponseEntity<Page<WhouseResponse>> list(Pageable pageable,
                                                     @RequestParam(required = false) Boolean onlyActive,
                                                     @RequestParam(required = false) Long branchId) {
        return ResponseEntity.ok(service.list(pageable, onlyActive, branchId));
    }

    // LIST by company (admin use-case) + optional branch filter
    @GetMapping("/by-company")
    public ResponseEntity<Page<WhouseResponse>> listByCompany(@RequestParam Long companyId,
                                                              Pageable pageable,
                                                              @RequestParam(required = false) Boolean onlyActive,
                                                              @RequestParam(required = false) Long branchId) {
        return ResponseEntity.ok(service.listByCompany(companyId, pageable, onlyActive, branchId));
    }

    @GetMapping("/by-branch/{branchId}")
    public ResponseEntity<Page<WhouseResponse>> listByBranch(
            @PathVariable Long branchId,
            Pageable pageable,
            @RequestParam(required = false) Boolean onlyActive
    ) {
        return ResponseEntity.ok(service.listByBranch(branchId, pageable, onlyActive));
    }


    // GET by id
    @GetMapping("/{id}")
    public ResponseEntity<WhouseResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // CREATE
    @PostMapping
    public ResponseEntity<WhouseResponse> create(@Valid @RequestBody WhouseRequest req) {
        WhouseResponse created = service.create(req);
        return ResponseEntity.created(URI.create("/whouses/" + created.id())).body(created);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<WhouseResponse> update(@PathVariable Long id,
                                                 @Valid @RequestBody WhouseRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    // DELETE (soft)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
