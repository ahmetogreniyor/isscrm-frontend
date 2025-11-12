package com.isscrm.isscrm_backend.service;

import com.isscrm.isscrm_backend.repository.BillingRepository;
import com.isscrm.isscrm_backend.repository.CustomerRepository;
import com.isscrm.isscrm_backend.repository.DealerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final BillingRepository billingRepository;
    private final DealerRepository dealerRepository;
    private final CustomerRepository customerRepository;

    /**
     * Gathers summary statistics for the dashboard view.
     */
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        Double totalPaid = billingRepository.sumTotalPaidAmount();
        Double totalUnpaid = billingRepository.sumTotalUnpaidAmount();
        Long totalDealers = dealerRepository.count();
        Long totalCustomers = customerRepository.count();

        stats.put("totalPaidAmount", totalPaid != null ? totalPaid : 0.0);
        stats.put("totalUnpaidAmount", totalUnpaid != null ? totalUnpaid : 0.0);
        stats.put("totalDealers", totalDealers);
        stats.put("totalCustomers", totalCustomers);

        return stats;
    }
}
