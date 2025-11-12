package com.isscrm.isscrm_backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mikrotik_sync_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MikroTikSyncLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String entityType;   // Örneğin: Tariff, Customer, Dealer
    private Long entityId;       // İlgili kaydın ID’si
    private String action;       // "CREATE", "UPDATE", "DELETE"
    private String status;       // "SUCCESS", "ERROR"
    private String message;      // Log açıklaması

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
