package com.isscrm.isscrm_backend.service;

import com.isscrm.isscrm_backend.model.Customer;
import com.isscrm.isscrm_backend.model.Dealer;
import com.isscrm.isscrm_backend.repository.CustomerRepository;
import com.isscrm.isscrm_backend.repository.DealerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DealerService {

    private final DealerRepository dealerRepository;
    private final CustomerRepository customerRepository;

    public DealerService(DealerRepository dealerRepository, CustomerRepository customerRepository) {
        this.dealerRepository = dealerRepository;
        this.customerRepository = customerRepository;
    }

    // 🟢 CREATE
    public Dealer createDealer(Dealer dealer) {
        return dealerRepository.save(dealer);
    }

    // 🟡 READ - All
    public List<Dealer> getAllDealers() {
        return dealerRepository.findAll();
    }

    // 🔵 READ - By ID
    public Dealer getDealerById(Long id) {
        return dealerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dealer not found with id: " + id));
    }

    // 🟣 UPDATE
    public Dealer updateDealer(Long id, Dealer dealerDetails) {
        Dealer existingDealer = dealerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dealer not found with id: " + id));

        existingDealer.setDealerCode(dealerDetails.getDealerCode());
        existingDealer.setDealerName(dealerDetails.getDealerName());
        existingDealer.setCategory(dealerDetails.getCategory());
        existingDealer.setCreditLimit(dealerDetails.getCreditLimit());
        existingDealer.setBalance(dealerDetails.getBalance());

        return dealerRepository.save(existingDealer);
    }

    // 🔴 DELETE
    public void deleteDealer(Long id) {
        dealerRepository.deleteById(id);
    }

    // 👥 Customers by Dealer
    public List<Customer> getDealerCustomers(Long dealerId) {
        Dealer dealer = dealerRepository.findById(dealerId)
                .orElseThrow(() -> new RuntimeException("Dealer not found with id: " + dealerId));
        return customerRepository.findByDealer(dealer);
    }
}
