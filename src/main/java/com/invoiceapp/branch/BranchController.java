package com.invoiceapp.branch;

import com.invoiceapp.branch.dto.BranchRequest;
import com.invoiceapp.branch.dto.BranchResponse;
import com.invoiceapp.companyscope.RequireTenant;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/branches")
@RequireTenant
public class BranchController {

    private final BranchService service;

    public BranchController(BranchService service) {
        this.service = service;
    }

    // LIST (τρέχων tenant, με φίλτρο onlyActive)
    @GetMapping
    public ResponseEntity<Page<BranchResponse>> list(Pageable pageable,
                                                     @RequestParam(required = false) Boolean onlyActive) {
        return ResponseEntity.ok(service.list(pageable, onlyActive));
    }

    // LIST by company (admin use-case)
    @GetMapping("/by-company")
    public ResponseEntity<Page<BranchResponse>> listByCompany(@RequestParam Long companyId,
                                                              Pageable pageable,
                                                              @RequestParam(required = false) Boolean onlyActive) {
        return ResponseEntity.ok(service.listByCompany(companyId, pageable, onlyActive));
    }

    // LIST headquarters για εταιρεία
    @GetMapping("/headquarters")
    public ResponseEntity<List<BranchResponse>> listHeadquarters(@RequestParam Long companyId) {
        return ResponseEntity.ok(service.listHeadquarters(companyId));
    }

    // GET by id
    @GetMapping("/{id}")
    public ResponseEntity<BranchResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // GET by code
    @GetMapping("/code/{code}")
    public ResponseEntity<BranchResponse> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(service.getByCode(code));
    }

    // CREATE
    @PostMapping
    public ResponseEntity<BranchResponse> create(@Valid @RequestBody BranchRequest req) {
        BranchResponse created = service.create(req);
        return ResponseEntity.created(URI.create("/branches/" + created.id())).body(created);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<BranchResponse> update(@PathVariable Long id,
                                                 @Valid @RequestBody BranchRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    // DELETE (soft)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
