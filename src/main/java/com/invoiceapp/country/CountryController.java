package com.invoiceapp.country;

import com.invoiceapp.country.dto.CountryRequest;
import com.invoiceapp.country.dto.CountryResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/countries")
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    // GET by ID
    @GetMapping("/{id}")
    public ResponseEntity<CountryResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(countryService.getById(id));
    }

    // GET by isoCode
    @GetMapping("/iso/{isoCode}")
    public ResponseEntity<CountryResponse> getByIsoCode(@PathVariable String isoCode) {
        return ResponseEntity.ok(countryService.getByIsoCode(isoCode));
    }

    // CREATE
    @PostMapping
    public ResponseEntity<CountryResponse> create(@Valid @RequestBody CountryRequest request) {
        CountryResponse created = countryService.create(request);
        return ResponseEntity
                .created(URI.create("/countries/" + created.id()))
                .body(created);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<CountryResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody CountryRequest request) {
        return ResponseEntity.ok(countryService.update(id, request));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        countryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
