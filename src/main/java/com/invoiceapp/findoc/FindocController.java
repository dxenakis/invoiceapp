package com.invoiceapp.findoc;

import com.invoiceapp.companyscope.RequireTenant;
import com.invoiceapp.findoc.dto.FindocCreateRequest;
import com.invoiceapp.findoc.dto.FindocResponse;
import com.invoiceapp.findoc.dto.MtrLineRequest;
import com.invoiceapp.global.DocumentStatus;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/findoc")
@RequireTenant
public class FindocController {

    private final FindocService service;

    public FindocController(FindocService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<FindocResponse> createDraft(@Valid @RequestBody FindocCreateRequest req) {
        FindocResponse created = service.createDraft(req);
        return ResponseEntity.created(URI.create("/findoc/" + created.id())).body(created);
    }

    @PostMapping("/{id}/lines")
    public ResponseEntity<FindocResponse> upsertLine(@PathVariable Long id,
                                                     @Valid @RequestBody MtrLineRequest line) {
        return ResponseEntity.ok(service.upsertLine(id, line));
    }

    @DeleteMapping("/{id}/lines/{lineId}")
    public ResponseEntity<FindocResponse> deleteLine(@PathVariable Long id, @PathVariable Long lineId) {
        return ResponseEntity.ok(service.deleteLine(id, lineId));
    }

    @PostMapping("/{id}/post")
    public ResponseEntity<FindocResponse> post(@PathVariable Long id) {
        return ResponseEntity.ok(service.post(id));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<FindocResponse> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(service.cancel(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FindocResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping
    public ResponseEntity<Page<FindocResponse>> list(Pageable pageable,
                                                     @RequestParam(required = false) DocumentStatus status) {
        return ResponseEntity.ok(service.list(pageable, status));
    }
}
