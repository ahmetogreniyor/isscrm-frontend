package com.isscrm.isscrm_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "olo_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OloResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 📍 Türk Telekom sorgusundan gelen adres kodu
    private String addressCode;

    // 🏗️ Altyapı durum bilgileri
    private boolean fiberAvailable;
    private boolean vdslAvailable;
    private boolean adslAvailable;

    // ⚡ Maksimum desteklenen hız (örnek: "100 Mbps")
    private String maxSpeed;

    // 🗓️ Sorgu tarihi
    private String checkedAt;

    // 📌 Bölge bilgisi (opsiyonel)
    private String region;

    // 📡 Sonuç kaynağı (örnek: "TTNET OLO", "Manual")
    private String source;
}
