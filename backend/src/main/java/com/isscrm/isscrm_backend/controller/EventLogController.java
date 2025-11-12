package com.isscrm.isscrm_backend.controller;

import com.isscrm.isscrm_backend.model.EventLog;
import com.isscrm.isscrm_backend.service.EventLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/event-logs")
@RequiredArgsConstructor
public class EventLogController {

    private final EventLogService eventLogService;

    /**
     * 🔹 Son 50 log kaydını döner.
     * Örnek: GET /api/event-logs
     */
    @GetMapping
    public List<EventLog> getRecentLogs() {
        return eventLogService.getRecentLogs();
    }

    /**
     * 🔹 Manuel log ekleme için basit endpoint (isteğe bağlı).
     * Örnek:
     * POST /api/event-logs
     * {
     *   "eventType": "TEST_EVENT",
     *   "description": "Manual test log"
     * }
     */
    @PostMapping
    public EventLog createLog(@RequestBody EventLog eventLog) {
        eventLog.setActor("API");
        eventLog.setCreatedAt(java.time.LocalDateTime.now());
        return eventLogService.save(eventLog);
    }
}
