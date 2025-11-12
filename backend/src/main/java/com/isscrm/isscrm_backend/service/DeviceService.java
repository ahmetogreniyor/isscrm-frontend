package com.isscrm.isscrm_backend.service;

import com.isscrm.isscrm_backend.model.Device;
import com.isscrm.isscrm_backend.repository.DeviceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    // 🧾 Tüm cihazları listele
    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    // 🔍 ID ile cihaz bul
    public Device getDeviceById(Long id) {
        return deviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Device not found with id: " + id));
    }

    // 🔗 Belirli bir dealer'a ait cihazları getir
    public List<Device> getDevicesByDealer(Long dealerId) {
        return deviceRepository.findByDealerId(dealerId);
    }

    // ➕ Yeni cihaz kaydet
    public Device saveDevice(Device device) {
        return deviceRepository.save(device);
    }

    // 🔄 Güncelleme
    public Device updateDevice(Long id, Device updated) {
        Device existing = getDeviceById(id);
        existing.setName(updated.getName());
        existing.setIpAddress(updated.getIpAddress());
        existing.setMacAddress(updated.getMacAddress());
        existing.setVendor(updated.getVendor());
        existing.setModel(updated.getModel());
        existing.setRegion(updated.getRegion());
        existing.setManagementProtocol(updated.getManagementProtocol());
        existing.setStatus(updated.getStatus());
        return deviceRepository.save(existing);
    }

    // ❌ Cihaz sil
    public void deleteDevice(Long id) {
        deviceRepository.deleteById(id);
    }
}
