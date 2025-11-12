package com.isscrm.isscrm_backend.repository;

import com.isscrm.isscrm_backend.model.EventLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventLogRepository extends JpaRepository<EventLog, Long> {

    // 🔹 Dashboard için son 10 log
    List<EventLog> findTop10ByOrderByCreatedAtDesc();

    // 🔹 EventLogService içinde kullanılabilecek ek method
    List<EventLog> findTop50ByOrderByCreatedAtDesc();
}
