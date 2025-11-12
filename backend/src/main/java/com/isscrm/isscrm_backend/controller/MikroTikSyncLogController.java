package com.isscrm.isscrm_backend.controller;

import com.isscrm.isscrm_backend.model.MikroTikSyncLog;
import com.isscrm.isscrm_backend.service.MikroTikSyncLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mikrotik/logs")
@RequiredArgsConstructor
public class MikroTikSyncLogController {

    private final MikroTikSyncLogService logService;

    @GetMapping
    public List<MikroTikSyncLog> getAll() {
        return logService.getAllLogs();
    }

    @GetMapping("/{entityType}/{entityId}")
    public List<MikroTikSyncLog> getByEntity(@PathVariable String entityType, @PathVariable Long entityId) {
        return logService.getLogsByEntity(entityType, entityId);
    }
}
