package com.isscrm.isscrm_backend.service;

import com.isscrm.isscrm_backend.model.Tariff;
import com.isscrm.isscrm_backend.repository.TariffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TariffService {

    private final TariffRepository tariffRepository;
    private final MikroTikSyncLogService mikroTikSyncLogService;

    public List<Tariff> getAllTariffs() {
        return tariffRepository.findAll();
    }

    public Tariff createTariff(Tariff tariff) {
        try {
            Tariff saved = tariffRepository.save(tariff);
            mikroTikSyncLogService.logSuccess("Tariff", saved.getId(), "CREATE", "Tariff successfully created");
            return saved;
        } catch (Exception e) {
            mikroTikSyncLogService.logError("Tariff", null, "CREATE", e.getMessage());
            throw e;
        }
    }

    public Tariff updateTariff(Long id, Tariff updatedTariff) {
        return tariffRepository.findById(id).map(existing -> {
            existing.setName(updatedTariff.getName());
            existing.setSpeedMbps(updatedTariff.getSpeedMbps());
            existing.setQuotaGb(updatedTariff.getQuotaGb());
            existing.setPrice(updatedTariff.getPrice());
            existing.setCategory(updatedTariff.getCategory());
            Tariff saved = tariffRepository.save(existing);
            mikroTikSyncLogService.logSuccess("Tariff", id, "UPDATE", "Tariff updated successfully");
            return saved;
        }).orElseThrow(() -> {
            mikroTikSyncLogService.logError("Tariff", id, "UPDATE", "Tariff not found");
            return new RuntimeException("Tariff not found");
        });
    }

    public void deleteTariff(Long id) {
        try {
            tariffRepository.deleteById(id);
            mikroTikSyncLogService.logSuccess("Tariff", id, "DELETE", "Tariff deleted successfully");
        } catch (Exception e) {
            mikroTikSyncLogService.logError("Tariff", id, "DELETE", e.getMessage());
            throw e;
        }
    }
}
