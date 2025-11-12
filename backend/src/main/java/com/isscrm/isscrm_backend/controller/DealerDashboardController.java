package com.isscrm.isscrm_backend.controller;

import com.isscrm.isscrm_backend.model.Billing;
import com.isscrm.isscrm_backend.service.DealerDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dealer-dashboard")
@RequiredArgsConstructor
public class DealerDashboardController {

    private final DealerDashboardService dealerDashboardService;

    @GetMapping("/{dealerId}/summary")
    public Map<String, Object> getDealerSummary(@PathVariable Long dealerId) {
        return dealerDashboardService.getDealerDashboardSummary(dealerId);
    }

    @GetMapping("/{dealerId}/billings")
    public List<Billing> getDealerBillings(@PathVariable Long dealerId) {
        return dealerDashboardService.getDealerBillings(dealerId);
    }
}
