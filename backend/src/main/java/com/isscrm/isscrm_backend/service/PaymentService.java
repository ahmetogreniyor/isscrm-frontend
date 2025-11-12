package com.isscrm.isscrm_backend.service;

import com.isscrm.isscrm_backend.model.Payment;
import com.isscrm.isscrm_backend.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository repo;

    public PaymentService(PaymentRepository repo) {
        this.repo = repo;
    }

    public List<Payment> getAll() {
        return repo.findAll();
    }

    public Payment save(Payment p) {
        return repo.save(p);
    }

    public List<Payment> getTodayPayments() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(23, 59, 59);
        return repo.findByCreatedAtBetween(start, end);
    }

    public Double getTodayTotal() {
        return getTodayPayments().stream()
                .mapToDouble(Payment::getAmount)
                .sum();
    }
}
