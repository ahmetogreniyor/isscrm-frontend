package com.isscrm.isscrm_backend.repository;

import com.isscrm.isscrm_backend.model.MikroTikSyncLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MikroTikSyncLogRepository extends JpaRepository<MikroTikSyncLog, Long> {

    List<MikroTikSyncLog> findByEntityTypeAndEntityId(String entityType, Long entityId);
}
