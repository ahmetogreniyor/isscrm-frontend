package com.isscrm.isscrm_backend.repository;

import com.isscrm.isscrm_backend.model.DeviceJob;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DeviceJobRepository extends JpaRepository<DeviceJob, Long> {
    List<DeviceJob> findByDeviceId(Long deviceId);
    List<DeviceJob> findByStatus(String status);
}
