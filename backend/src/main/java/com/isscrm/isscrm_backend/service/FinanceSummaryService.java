package com.isscrm.isscrm_backend.service;

import com.isscrm.isscrm_backend.model.Invoice;
import com.isscrm.isscrm_backend.repository.InvoiceRepository;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FinanceSummaryService {

    private final InvoiceRepository invoiceRepository;

    public FinanceSummaryService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    // 📊 1️⃣ Tüm faturaların toplam gelirini döner
    public double getTotalRevenue() {
        return invoiceRepository.findAll().stream()
                .mapToDouble(Invoice::getAmount)
                .sum();
    }

    // 📆 2️⃣ Aylık bazda toplam gelir raporu
    public Map<String, Double> getMonthlyRevenue() {
        return invoiceRepository.findAll().stream()
                .filter(inv -> inv.getIssueDate() != null)
                .collect(Collectors.groupingBy(
                        inv -> inv.getIssueDate().getMonth().toString(),
                        Collectors.summingDouble(Invoice::getAmount)
                ));
    }

    // 🧾 3️⃣ Durum bazlı toplamlar (örnek: PAID / UNPAID)
    public Map<String, Double> getStatusSummary() {
        return invoiceRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        inv -> inv.getStatus() != null ? inv.getStatus() : "UNKNOWN",
                        Collectors.summingDouble(Invoice::getAmount)
                ));
    }

    // 🧮 4️⃣ En yüksek tutarlı faturayı döner
    public Optional<Invoice> getHighestInvoice() {
        return invoiceRepository.findAll().stream()
                .max(Comparator.comparingDouble(Invoice::getAmount));
    }

    // 📉 5️⃣ En düşük tutarlı faturayı döner
    public Optional<Invoice> getLowestInvoice() {
        return invoiceRepository.findAll().stream()
                .min(Comparator.comparingDouble(Invoice::getAmount));
    }

    // 🔄 6️⃣ Belirli bir ayın toplam gelirini getirir
    public double getRevenueByMonth(Month month) {
        return invoiceRepository.findAll().stream()
                .filter(inv -> inv.getIssueDate() != null &&
                        inv.getIssueDate().getMonth() == month)
                .mapToDouble(Invoice::getAmount)
                .sum();
    }
}
