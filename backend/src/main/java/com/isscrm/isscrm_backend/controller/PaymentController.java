package com.isscrm.isscrm_backend.controller;

import com.isscrm.isscrm_backend.model.Payment;
import com.isscrm.isscrm_backend.service.PaymentService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @GetMapping
    public List<Payment> getAll() {
        return service.getAll();
    }

    @PostMapping
    public Payment create(@RequestBody Payment payment) {
        return service.save(payment);
    }

    @GetMapping("/today")
    public Map<String, Object> getTodaySummary() {
        Map<String, Object> response = new HashMap<>();
        response.put("total", service.getTodayTotal());
        response.put("payments", service.getTodayPayments());
        return response;
    }
}
