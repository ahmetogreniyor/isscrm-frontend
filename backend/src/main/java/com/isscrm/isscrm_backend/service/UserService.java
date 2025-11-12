package com.isscrm.isscrm_backend.service;

import com.isscrm.isscrm_backend.model.User;
import com.isscrm.isscrm_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 🔹 1️⃣ Tüm kullanıcıları getir
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 🔹 2️⃣ ID'ye göre kullanıcı getir
    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // 🔹 3️⃣ Email’e göre kullanıcı bul (login için)
    public User findByEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        return optionalUser.orElse(null);
    }

    // 🔹 4️⃣ Kullanıcı ekle (register veya admin ekleme)
    public User addUser(User user) {
        // Aynı email varsa hata
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists: " + user.getEmail());
        }

        // Role boşsa varsayılan USER
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        }

        return userRepository.save(user);
    }

    // 🔹 5️⃣ Kullanıcı güncelle veya kaydet (Profile update)
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // 🔹 6️⃣ ID ile kullanıcıyı bul (update işlemi için)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    // 🔹 7️⃣ Kullanıcı sil
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // 🔹 8️⃣ Login işlemi (AuthController için)
    public User login(String email, String password) {
        User user = findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }

    // 🔹 9️⃣ Kullanıcı oluşturma (register için)
    public User createUser(User user) {
        return addUser(user);
    }
}
