package com.isscrm.isscrm_backend.service;

import com.isscrm.isscrm_backend.model.Customer;
import com.isscrm.isscrm_backend.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // 🟢 CREATE
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    // 🟡 READ - All
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // 🔵 READ - By ID
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
    }

    // 🟣 UPDATE
    public Customer updateCustomer(Long id, Customer customerDetails) {
        Customer existing = getCustomerById(id);

        existing.setFullName(customerDetails.getFullName());
        existing.setEmail(customerDetails.getEmail());
        existing.setPhone(customerDetails.getPhone());
        existing.setAddress(customerDetails.getAddress());
        existing.setUsername(customerDetails.getUsername());
        existing.setPassword(customerDetails.getPassword());
        existing.setActive(customerDetails.isActive());
        existing.setDealer(customerDetails.getDealer());
        existing.setTariff(customerDetails.getTariff());

        return customerRepository.save(existing);
    }

    // 🔴 DELETE
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}
