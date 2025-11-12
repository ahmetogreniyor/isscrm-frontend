package com.isscrm.isscrm_backend.repository;

import com.isscrm.isscrm_backend.model.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Long> {

    // ---- Dashboard & Dealer Dashboard Queries ----

    @Query("SELECT SUM(b.amount) FROM Billing b WHERE b.status = 'PAID'")
    Double sumTotalPaidAmount();

    @Query("SELECT SUM(b.amount) FROM Billing b WHERE b.status = 'UNPAID'")
    Double sumTotalUnpaidAmount();

    // ---- Common Queries ----

    List<Billing> findByDealerId(Long dealerId);

    List<Billing> findByCustomerId(Long customerId);

    List<Billing> findByStatus(String status);
}
