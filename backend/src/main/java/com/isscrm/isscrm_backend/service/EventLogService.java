package com.isscrm.isscrm_backend.service;

import com.isscrm.isscrm_backend.model.EventLog;
import com.isscrm.isscrm_backend.repository.EventLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventLogService {

    private final EventLogRepository eventLogRepository;

    /**
     * 🔹 Yeni bir log kaydı oluşturur
     */
    public void log(String eventType, String description) {
        EventLog log = new EventLog();
        log.setEventType(eventType);
        log.setDescription(description);
        log.setActor("SYSTEM");
        log.setCreatedAt(LocalDateTime.now());
        eventLogRepository.save(log);
    }

    /**
     * 🔹 Dışarıdan EventLogController tarafından çağrılabilir save()
     */
    public EventLog save(EventLog log) {
        return eventLogRepository.save(log);
    }

    /**
     * 🔹 Son 50 log kaydını getirir
     */
    public List<EventLog> getRecentLogs() {
        return eventLogRepository.findTop10ByOrderByCreatedAtDesc();
    }
}
