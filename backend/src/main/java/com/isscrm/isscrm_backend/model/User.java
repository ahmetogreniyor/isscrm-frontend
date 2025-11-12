package com.isscrm.isscrm_backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * ISS CRM kullanıcı entity'si.
 * PostgreSQL 'users' tablosuyla eşleşir.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔹 Kullanıcının tam adı
    @Column(nullable = true, length = 100)
    private String fullName;

    // 🔹 Email benzersiz ve zorunlu
    @Column(unique = true, nullable = false, length = 100)
    private String email;

    // 🔹 Telefon isteğe bağlı
    @Column(length = 20)
    private String phone;

    // 🔹 Kullanıcı adı (opsiyonel, ama unique)
    @Column(unique = true, nullable = true, length = 50)
    private String username;

    // 🔹 Şifre (daha sonra BCrypt hash kullanılacak)
    @Column(nullable = true, length = 255)
    private String password;

    // 🔹 Rol (USER / ADMIN)
    @Column(nullable = false, length = 20)
    private String role = "USER";

    // 🔹 Kayıt zamanı
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // --- Constructors ---
    public User() {
    }

    public User(String fullName, String email, String phone, String username, String password, String role) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.role = role;
        this.createdAt = LocalDateTime.now();
    }

    // --- Getters & Setters ---
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
