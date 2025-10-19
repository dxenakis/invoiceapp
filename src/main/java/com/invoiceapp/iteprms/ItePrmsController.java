package com.invoiceapp.iteprms;


import com.invoiceapp.global.DocumentDomain;
import com.invoiceapp.iteprms.dto.ItePrmsCreateRequest;
import com.invoiceapp.iteprms.dto.ItePrmsUpdateRequest;
import com.invoiceapp.tprms.Tprms;
import com.invoiceapp.tprms.TprmsService;
import com.invoiceapp.tprms.dto.TprmsCreateRequest;
import com.invoiceapp.tprms.dto.TprmsUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/iteprms")
public class ItePrmsController {

    private final ItePrmsService service;

    public ItePrmsController(ItePrmsService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItePrms create(@RequestBody ItePrmsCreateRequest req) {
        return service.create(req);
    }

    @GetMapping
    public Page<ItePrms> list(Pageable pageable,
                            @RequestParam(defaultValue = "false") boolean onlyActive) {
        return service.list(pageable, onlyActive);
    }

    @GetMapping("/{id}")
    public ItePrms get(@PathVariable Long id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    public ItePrms update(@PathVariable Long id, @RequestBody ItePrmsUpdateRequest req) {
        return service.update(id, req);
    }
    @GetMapping("/{domain}")
    public List<ItePrms> getByDomain(@PathVariable DocumentDomain domain) {
        return service.findAllByDomain(domain);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }


}
