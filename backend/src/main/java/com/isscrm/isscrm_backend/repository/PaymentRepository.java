package com.isscrm.isscrm_backend.repository;

import com.isscrm.isscrm_backend.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
