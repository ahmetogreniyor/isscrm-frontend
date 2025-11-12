package com.isscrm.isscrm_backend.controller;

import com.isscrm.isscrm_backend.model.Invoice;
import com.isscrm.isscrm_backend.service.FinanceSummaryService;
import org.springframework.web.bind.annotation.*;

import java.time.Month;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/finance/summary")
public class FinanceSummaryController {

    private final FinanceSummaryService financeSummaryService;

    public FinanceSummaryController(FinanceSummaryService financeSummaryService) {
        this.financeSummaryService = financeSummaryService;
    }

    // 📊 1️⃣ Tüm faturaların toplam gelirini getir
    @GetMapping("/total")
    public double getTotalRevenue() {
        return financeSummaryService.getTotalRevenue();
    }

    // 📆 2️⃣ Aylık bazda toplam gelir
    @GetMapping("/monthly")
    public Map<String, Double> getMonthlyRevenue() {
        return financeSummaryService.getMonthlyRevenue();
    }

    // 🧾 3️⃣ Durum bazlı özet (PAID / PENDING / UNPAID)
    @GetMapping("/status")
    public Map<String, Double> getStatusSummary() {
        return financeSummaryService.getStatusSummary();
    }

    // 💰 4️⃣ En yüksek tutarlı faturayı getir
    @GetMapping("/highest")
    public Optional<Invoice> getHighestInvoice() {
        return financeSummaryService.getHighestInvoice();
    }

    // 💸 5️⃣ En düşük tutarlı faturayı getir
    @GetMapping("/lowest")
    public Optional<Invoice> getLowestInvoice() {
        return financeSummaryService.getLowestInvoice();
    }

    // 🗓️ 6️⃣ Belirli bir ayın toplam gelirini getir
    @GetMapping("/month/{month}")
    public double getRevenueByMonth(@PathVariable String month) {
        try {
            Month targetMonth = Month.valueOf(month.toUpperCase());
            return financeSummaryService.getRevenueByMonth(targetMonth);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("❌ Invalid month name. Example: JANUARY, FEBRUARY, MARCH");
        }
    }
}
