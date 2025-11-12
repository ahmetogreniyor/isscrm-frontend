package com.isscrm.isscrm_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tariffs")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Tariff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer speedMbps;

    @Column(nullable = true)
    private Integer quotaGb;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = true)
    private String category; // örn: Fiber / VDSL / Wireless

    @OneToMany(mappedBy = "tariff", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"tariff"})
    private List<Customer> customers;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Tariff() {}

    public Tariff(String name, Integer speedMbps, Integer quotaGb, Double price, String category) {
        this.name = name;
        this.speedMbps = speedMbps;
        this.quotaGb = quotaGb;
        this.price = price;
        this.category = category;
        this.createdAt = LocalDateTime.now();
    }

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getSpeedMbps() { return speedMbps; }
    public void setSpeedMbps(Integer speedMbps) { this.speedMbps = speedMbps; }

    public Integer getQuotaGb() { return quotaGb; }
    public void setQuotaGb(Integer quotaGb) { this.quotaGb = quotaGb; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public List<Customer> getCustomers() { return customers; }
    public void setCustomers(List<Customer> customers) { this.customers = customers; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
