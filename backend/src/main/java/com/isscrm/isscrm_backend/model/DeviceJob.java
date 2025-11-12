package com.isscrm.isscrm_backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String jobType;         // Örnek: "PING", "BACKUP", "CONFIG_PUSH"
    private String status;          // RUNNING, SUCCESS, FAILED
    private String resultMessage;   // Sonuç mesajı
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;

    // ✅ Hata çözen metotlar eklendi
    public void markRunning() {
        this.status = "RUNNING";
        this.updatedAt = LocalDateTime.now();
    }

    public void markSuccess(String message) {
        this.status = "SUCCESS";
        this.resultMessage = message;
        this.updatedAt = LocalDateTime.now();
    }

    public void markFailed(String reason) {
        this.status = "FAILED";
        this.resultMessage = reason;
        this.updatedAt = LocalDateTime.now();
    }
}
