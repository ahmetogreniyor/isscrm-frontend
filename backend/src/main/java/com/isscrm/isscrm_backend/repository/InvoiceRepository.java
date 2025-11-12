package com.isscrm.isscrm_backend.repository;

import com.isscrm.isscrm_backend.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    /**
     * 🔍 İki tarih arası fatura listesini döndürür
     */
    @Query("SELECT i FROM Invoice i WHERE i.issueDate BETWEEN :startDate AND :endDate")
    List<Invoice> findInvoicesBetweenDates(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 💰 Duruma göre (ör. 'PAID', 'PENDING') fatura listesini döndürür
     */
    List<Invoice> findByStatus(String status);

    /**
     * 📄 Fatura numarasına göre fatura döndürür
     */
    Invoice findByInvoiceNo(String invoiceNo);
}
