package com.isscrm.isscrm_backend.repository;

import com.isscrm.isscrm_backend.model.Customer;
import com.isscrm.isscrm_backend.model.Dealer;
import com.isscrm.isscrm_backend.model.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // 🔹 Aktif müşteri sayısı
    long countByActive(boolean active);

    // 🔹 Dealer bazlı müşteri listesi
    List<Customer> findByDealer(Dealer dealer);

    // 🔹 Tarife bazlı müşteri listesi
    List<Customer> findByTariff(Tariff tariff);

    // 🔹 Aktif müşteri listesi
    List<Customer> findByActive(boolean active);
}
