package com.isscrm.isscrm_backend.worker;

import com.isscrm.isscrm_backend.model.Device;
import com.isscrm.isscrm_backend.model.DeviceJob;
import com.isscrm.isscrm_backend.repository.DeviceJobRepository;
import com.isscrm.isscrm_backend.repository.DeviceRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Component
public class DeviceJobExecutor {

    private static final Logger log = LoggerFactory.getLogger(DeviceJobExecutor.class);

    private final DeviceJobRepository jobRepo;
    private final DeviceRepository deviceRepo;
    private final SimpMessagingTemplate messagingTemplate;

    public DeviceJobExecutor(DeviceJobRepository jobRepo,
                             DeviceRepository deviceRepo,
                             SimpMessagingTemplate messagingTemplate) {
        this.jobRepo = jobRepo;
        this.deviceRepo = deviceRepo;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Her 10 saniyede bir PENDING job'ları tarar ve sırayla çalıştırır.
     */
    @Scheduled(fixedDelay = 10000)
    public void processPendingJobs() {
        List<DeviceJob> pendingJobs = jobRepo.findByStatus("PENDING");

        if (pendingJobs.isEmpty()) {
            log.debug("⏳ Şu anda işlenecek pending job yok.");
            return;
        }

        log.info("🕒 {} adet pending job bulundu, işleniyor...", pendingJobs.size());

        for (DeviceJob job : pendingJobs) {
            try {
                job.markRunning();
                jobRepo.save(job);
                messagingTemplate.convertAndSend("/topic/jobs", job);
                log.info("▶️ Job başladı: {} (Device ID: {})", job.getJobType(), job.getDevice().getId());

                Device device = job.getDevice();
                String result = executeJob(job, device);

                job.markSuccess(result);
                jobRepo.save(job);
                messagingTemplate.convertAndSend("/topic/jobs", job);

                log.info("✅ Job başarıyla tamamlandı: {} ({})", job.getJobType(), device.getName());

            } catch (Exception e) {
                job.markFailed(e.getMessage());
                jobRepo.save(job);
                messagingTemplate.convertAndSend("/topic/jobs", job);

                log.error("❌ Job başarısız: {} - Hata: {}", job.getJobType(), e.getMessage());
            }
        }
    }

    /**
     * Mock işlem simülasyonu (gerçek cihaz bağlantısı ileride buraya gelecek)
     */
    private String executeJob(DeviceJob job, Device device) throws Exception {
        String type = job.getJobType();

        switch (type) {
            case "CHANGE_WIFI_PSK":
                log.debug("🔧 Wi-Fi şifresi değiştiriliyor: {}", device.getName());
                Thread.sleep(2000); // işlem simülasyonu
                return "Wi-Fi şifresi başarıyla değiştirildi.";

            case "REBOOT":
                log.debug("🔁 Cihaz yeniden başlatılıyor: {}", device.getName());
                Thread.sleep(1500);
                return "Cihaz yeniden başlatıldı.";

            case "UPDATE_SPEED":
                log.debug("⚙️ Hız limiti güncelleniyor: {}", device.getName());
                Thread.sleep(1000);
                return "Hız limiti başarıyla güncellendi.";

            default:
                throw new Exception("Bilinmeyen job tipi: " + type);
        }
    }
}
