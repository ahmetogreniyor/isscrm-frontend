package com.isscrm.isscrm_backend.service;

import com.isscrm.isscrm_backend.model.MikroTikSyncLog;
import com.isscrm.isscrm_backend.repository.MikroTikSyncLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MikroTikSyncLogService {

    private final MikroTikSyncLogRepository mikroTikSyncLogRepository;

    public void logSuccess(String entityType, Long entityId, String action, String message) {
        MikroTikSyncLog log = new MikroTikSyncLog();
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setAction(action);
        log.setMessage("SUCCESS: " + message);
        log.setCreatedAt(LocalDateTime.now());
        mikroTikSyncLogRepository.save(log);
    }

    public void logError(String entityType, Long entityId, String action, String message) {
        MikroTikSyncLog log = new MikroTikSyncLog();
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setAction(action);
        log.setMessage("ERROR: " + message);
        log.setCreatedAt(LocalDateTime.now());
        mikroTikSyncLogRepository.save(log);
    }

    public List<MikroTikSyncLog> getAllLogs() {
        return mikroTikSyncLogRepository.findAll();
    }

    public List<MikroTikSyncLog> getLogsByEntity(String entityType, Long entityId) {
        return mikroTikSyncLogRepository.findByEntityTypeAndEntityId(entityType, entityId);
    }
}
