package com.isscrm.isscrm_backend.controller;

import com.isscrm.isscrm_backend.model.Customer;
import com.isscrm.isscrm_backend.model.Dealer;
import com.isscrm.isscrm_backend.service.DealerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dealers")
@CrossOrigin(origins = "http://localhost:5173") // Frontend erişimi için
public class DealerController {

    private final DealerService dealerService;

    public DealerController(DealerService dealerService) {
        this.dealerService = dealerService;
    }

    // 🟢 CREATE
    @PostMapping
    public ResponseEntity<Dealer> createDealer(@RequestBody Dealer dealer) {
        Dealer savedDealer = dealerService.createDealer(dealer);
        return ResponseEntity.ok(savedDealer);
    }

    // 🟡 READ - All
    @GetMapping
    public ResponseEntity<List<Dealer>> getAllDealers() {
        return ResponseEntity.ok(dealerService.getAllDealers());
    }

    // 🔵 READ - By ID
    @GetMapping("/{id}")
    public ResponseEntity<Dealer> getDealerById(@PathVariable Long id) {
        return ResponseEntity.ok(dealerService.getDealerById(id));
    }

    // 🟣 UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Dealer> updateDealer(@PathVariable Long id, @RequestBody Dealer dealerDetails) {
        Dealer updatedDealer = dealerService.updateDealer(id, dealerDetails);
        return ResponseEntity.ok(updatedDealer);
    }

    // 🔴 DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDealer(@PathVariable Long id) {
        dealerService.deleteDealer(id);
        return ResponseEntity.ok("Dealer deleted successfully.");
    }

    // 👥 Customers by Dealer
    @GetMapping("/{id}/customers")
    public ResponseEntity<List<Customer>> getDealerCustomers(@PathVariable Long id) {
        List<Customer> customers = dealerService.getDealerCustomers(id);
        return ResponseEntity.ok(customers);
    }
}
