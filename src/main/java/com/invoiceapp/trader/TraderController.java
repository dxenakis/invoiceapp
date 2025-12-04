package com.invoiceapp.trader;

import com.invoiceapp.global.enums.TraderDomain;
import com.invoiceapp.trader.dto.TraderRequestDto;
import com.invoiceapp.trader.dto.TraderResponseDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TraderController {

    private final TraderService traderService;

    public TraderController(TraderService traderService) {
        this.traderService = traderService;
    }

    // ---------- Customers endpoints ----------

    @GetMapping("/customers")
    public Page<TraderResponseDto> listCustomers(
            @RequestParam(required = false) String search,
            Pageable pageable
    ) {
        return traderService.listTraders(TraderDomain.CUSTOMER, search, pageable);
    }

    @GetMapping("/customers/{id}")
    public TraderResponseDto getCustomer(@PathVariable Long id) {
        return traderService.getTrader(id, TraderDomain.CUSTOMER);
    }

    @PostMapping("/customers")
    public TraderResponseDto createCustomer(
            @Valid @RequestBody TraderRequestDto request
    ) {
        return traderService.createTrader(TraderDomain.CUSTOMER, request);
    }

    @PutMapping("/customers/{id}")
    public TraderResponseDto updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody TraderRequestDto request
    ) {
        return traderService.updateTrader(id, TraderDomain.CUSTOMER, request);
    }

    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(@PathVariable Long id) {
        traderService.deleteTrader(id, TraderDomain.CUSTOMER);
    }

    // ---------- Suppliers endpoints ----------

    @GetMapping("/suppliers")
    public Page<TraderResponseDto> listSuppliers(
            @RequestParam(required = false) String search,
            Pageable pageable
    ) {
        return traderService.listTraders(TraderDomain.SUPPLIER, search, pageable);
    }

    @GetMapping("/suppliers/{id}")
    public TraderResponseDto getSupplier(@PathVariable Long id) {
        return traderService.getTrader(id, TraderDomain.SUPPLIER);
    }

    @PostMapping("/suppliers")
    public TraderResponseDto createSupplier(
            @Valid @RequestBody TraderRequestDto request
    ) {
        return traderService.createTrader(TraderDomain.SUPPLIER, request);
    }

    @PutMapping("/suppliers/{id}")
    public TraderResponseDto updateSupplier(
            @PathVariable Long id,
            @Valid @RequestBody TraderRequestDto request
    ) {
        return traderService.updateTrader(id, TraderDomain.SUPPLIER, request);
    }

    @DeleteMapping("/suppliers/{id}")
    public void deleteSupplier(@PathVariable Long id) {
        traderService.deleteTrader(id, TraderDomain.SUPPLIER);
    }
}
