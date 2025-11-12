package com.isscrm.isscrm_backend.service;

import com.isscrm.isscrm_backend.model.Billing;
import com.isscrm.isscrm_backend.repository.BillingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DealerDashboardService {

    private final BillingRepository billingRepository;

    public Map<String, Object> getDealerDashboardSummary(Long dealerId) {
        Map<String, Object> summary = new HashMap<>();

        List<Billing> dealerBillings = billingRepository.findByDealerId(dealerId);
        double totalRevenue = dealerBillings.stream()
                .mapToDouble(Billing::getAmount)
                .sum();

        long unpaidCount = dealerBillings.stream()
                .filter(b -> "UNPAID".equalsIgnoreCase(b.getStatus()))
                .count();

        summary.put("totalRevenue", totalRevenue);
        summary.put("unpaidInvoices", unpaidCount);
        summary.put("totalInvoices", dealerBillings.size());

        return summary;
    }

    public List<Billing> getDealerBillings(Long dealerId) {
        return billingRepository.findByDealerId(dealerId);
    }
}
