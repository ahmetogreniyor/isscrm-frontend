package com.isscrm.isscrm_backend.repository;

import com.isscrm.isscrm_backend.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findByDealerId(Long dealerId);
    List<Device> findByStatus(String status);
}
