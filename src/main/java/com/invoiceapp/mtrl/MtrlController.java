package com.invoiceapp.mtrl;

import com.invoiceapp.companyscope.RequireTenant;
import com.invoiceapp.documenttype.dto.DocumentTypeResponse;
import com.invoiceapp.mtrl.dto.MtrlRequest;
import com.invoiceapp.mtrl.dto.MtrlResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/items")
@RequireTenant // απαιτεί ενεργό tenant και σε επίπεδο controller
public class MtrlController {


    private final MtrlService service;


    public MtrlController(MtrlService service) {
        this.service = service;
    }


    // LIST
    @GetMapping
    public ResponseEntity<Page<MtrlResponse>> listAll(Pageable pageable) {
        return ResponseEntity.ok(service.listAll(pageable));
    }



    // GET
    @GetMapping("/{id}")
    public ResponseEntity<MtrlResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }


    // CREATE
    @PostMapping
    public ResponseEntity<MtrlResponse> create(@Valid @RequestBody MtrlRequest request) {
        MtrlResponse created = service.create(request);
        return ResponseEntity.created(URI.create("/items/" + created.id())).body(created);
    }


    // UPDATE (full/partial, εδώ χειριζόμαστε σαν PATCH μέσω πεδίων που δεν είναι null/0)
    @PutMapping("/{id}")
    public ResponseEntity<MtrlResponse> update(@PathVariable Long id,
                                       @Valid @RequestBody MtrlRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }


    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}