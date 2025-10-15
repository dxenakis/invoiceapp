package com.invoiceapp.item;

import com.invoiceapp.companyscope.RequireTenant;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/items")
@RequireTenant // απαιτεί ενεργό tenant και σε επίπεδο controller
public class ItemController {


    private final ItemService service;


    public ItemController(ItemService service) {
        this.service = service;
    }


    // LIST
    @GetMapping
    public ResponseEntity<List<Item>> listAll() {
        return ResponseEntity.ok(service.listAll());
    }


    // GET
    @GetMapping("/{id}")
    public ResponseEntity<Item> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }


    // CREATE
    @PostMapping
    public ResponseEntity<Item> create(@Valid @RequestBody Item request) {
        Item created = service.create(request);
        return ResponseEntity.created(URI.create("/items/" + created.getId())).body(created);
    }


    // UPDATE (full/partial, εδώ χειριζόμαστε σαν PATCH μέσω πεδίων που δεν είναι null/0)
    @PutMapping("/{id}")
    public ResponseEntity<Item> update(@PathVariable Long id,
                                       @Valid @RequestBody Item request) {
        return ResponseEntity.ok(service.update(id, request));
    }


    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}