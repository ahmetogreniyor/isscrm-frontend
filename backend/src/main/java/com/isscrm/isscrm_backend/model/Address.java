package com.isscrm.isscrm_backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String province;       // İl
    private String district;       // İlçe
    private String neighborhood;   // Mahalle
    private String street;         // Cadde/Sokak
    private String buildingNo;     // Bina No
    private String postalCode;     // Posta Kodu
    private String addressCode;    // BBK veya NVI adres kodu
    private String fullAddress;    // Tam adres metni

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Getters & Setters
}
