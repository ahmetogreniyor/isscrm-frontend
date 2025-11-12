package com.isscrm.isscrm_backend.controller;

import com.isscrm.isscrm_backend.model.User;
import com.isscrm.isscrm_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173") // React frontend bağlantısı için izin
public class UserController {

    @Autowired
    private UserService userService;

    // 🔹 1️⃣ Tüm kullanıcıları getir (Admin için)
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // 🔹 2️⃣ ID'ye göre kullanıcı getir
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUser(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    // 🔹 3️⃣ Email'e göre kullanıcı getir (Login sonrası ID bulmak için)
    @GetMapping("/by-email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    // 🔹 4️⃣ Yeni kullanıcı ekle (register veya admin)
    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User saved = userService.addUser(user);
        return ResponseEntity.ok(saved);
    }

    // 🔹 5️⃣ Kullanıcı güncelle (Profile.jsx ile)
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        Optional<User> optional = userService.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User existing = optional.get();

        // Güncellenebilir alanlar
        existing.setFullName(updatedUser.getFullName());
        existing.setPhone(updatedUser.getPhone());

        if (updatedUser.getRole() != null && !updatedUser.getRole().isEmpty()) {
            existing.setRole(updatedUser.getRole());
        }

        User saved = userService.saveUser(existing);
        return ResponseEntity.ok(saved);
    }

    // 🔹 6️⃣ Kullanıcı sil (Admin işlemi)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
