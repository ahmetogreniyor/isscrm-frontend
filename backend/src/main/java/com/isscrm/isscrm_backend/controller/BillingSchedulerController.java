package com.isscrm.isscrm_backend.controller;

import com.isscrm.isscrm_backend.scheduler.BillingScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scheduler/billing")
@RequiredArgsConstructor
public class BillingSchedulerController {

    private final BillingScheduler billingScheduler;

    @PostMapping("/generate")
    public ResponseEntity<String> generate() {
        billingScheduler.triggerManualBilling();
        return ResponseEntity.ok("Monthly billing generation triggered.");
    }
}
