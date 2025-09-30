package com.invoiceapp.company;

import com.invoiceapp.company.dto.CompanyCreateRequest;
import com.invoiceapp.company.dto.CompanyResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService service;

    public CompanyController(CompanyService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CompanyResponse> create(@Valid @RequestBody CompanyCreateRequest req) {
        return ResponseEntity.ok(service.createCompany(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(service.getCompanyById(id));
    }

    @GetMapping
    public ResponseEntity<List<CompanyResponse>> getAll() {
        return ResponseEntity.ok(service.getAllCompanies());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }
}
