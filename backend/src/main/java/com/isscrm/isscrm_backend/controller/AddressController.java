package com.isscrm.isscrm_backend.controller;

import com.isscrm.isscrm_backend.model.Address;
import com.isscrm.isscrm_backend.service.AddressService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/address")
@CrossOrigin(origins = "*")
public class AddressController {

    private final AddressService service;

    public AddressController(AddressService service) {
        this.service = service;
    }

    @GetMapping
    public List<Address> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Address getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/search")
    public List<Address> search(@RequestParam String query) {
        return service.search(query);
    }

    @PostMapping
    public Address create(@RequestBody Address address) {
        return service.save(address);
    }
}
