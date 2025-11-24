package com.invoiceapp.tprms;


import com.invoiceapp.findoc.enums.DocumentDomain;
import com.invoiceapp.tprms.dto.TprmsCreateRequest;
import com.invoiceapp.tprms.dto.TprmsUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tprms")
public class TprmsController {

    private final TprmsService service;

    public TprmsController(TprmsService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Tprms create(@RequestBody TprmsCreateRequest req) {
        return service.create(req);
    }

    @GetMapping
    public Page<Tprms> list(Pageable pageable,
                            @RequestParam(defaultValue = "false") boolean onlyActive) {
        return service.list(pageable, onlyActive);
    }

    @GetMapping("/{domain}")
    public List<Tprms> getByDomain(@PathVariable DocumentDomain domain) {
        return service.getByDomain(domain);
    }

    @GetMapping("/{id}")
    public Tprms get(@PathVariable Long id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    public Tprms update(@PathVariable Long id, @RequestBody TprmsUpdateRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
