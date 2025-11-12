package com.isscrm.isscrm_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "dealers")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Dealer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String dealerCode;

    @Column(nullable = false)
    private String dealerName;

    @Column(nullable = true)
    private String category; // GOLD, SILVER, BRONZE

    @Column(nullable = true)
    private Double creditLimit;

    @Column(nullable = true)
    private Double balance;

    @OneToMany(mappedBy = "dealer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"dealer"}) // Customer içindeki dealer’ı serialize etmez
    private List<Customer> customers;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Dealer() {}

    public Dealer(String dealerCode, String dealerName, String category, Double creditLimit, Double balance) {
        this.dealerCode = dealerCode;
        this.dealerName = dealerName;
        this.category = category;
        this.creditLimit = creditLimit;
        this.balance = balance;
        this.createdAt = LocalDateTime.now();
    }

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDealerCode() { return dealerCode; }
    public void setDealerCode(String dealerCode) { this.dealerCode = dealerCode; }

    public String getDealerName() { return dealerName; }
    public void setDealerName(String dealerName) { this.dealerName = dealerName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Double getCreditLimit() { return creditLimit; }
    public void setCreditLimit(Double creditLimit) { this.creditLimit = creditLimit; }

    public Double getBalance() { return balance; }
    public void setBalance(Double balance) { this.balance = balance; }

    public List<Customer> getCustomers() { return customers; }
    public void setCustomers(List<Customer> customers) { this.customers = customers; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
