package com.isscrm.isscrm_backend.controller;

import com.isscrm.isscrm_backend.model.Billing;
import com.isscrm.isscrm_backend.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/billings")
@RequiredArgsConstructor
public class BillingController {

    private final BillingService billingService;

    @PostMapping("/generate/{customerId}")
    public Billing generateBilling(@PathVariable Long customerId) {
        return billingService.generateBilling(customerId);
    }

    @PostMapping("/pay/{billingId}")
    public Billing payBilling(@PathVariable Long billingId) {
        return billingService.payBilling(billingId);
    }

    @GetMapping("/dealer/{dealerId}")
    public List<Billing> getBillingsByDealer(@PathVariable Long dealerId) {
        return billingService.getBillingsByDealer(dealerId);
    }

    @GetMapping("/customer/{customerId}")
    public List<Billing> getBillingsByCustomer(@PathVariable Long customerId) {
        return billingService.getBillingsByCustomer(customerId);
    }

    @GetMapping("/unpaid")
    public List<Billing> getUnpaidBillings() {
        return billingService.getUnpaidBillings();
    }

    @GetMapping("/summary/monthly")
    public String getMonthlySummary() {
        return billingService.getMonthlyBillingSummary();
    }

    @GetMapping("/export/csv")
    public ResponseEntity<String> exportBillingsToCsv() {
        String path = billingService.exportBillingsToCsv();
        return ResponseEntity.ok(path);
    }
}
