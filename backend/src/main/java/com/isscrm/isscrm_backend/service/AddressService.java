package com.isscrm.isscrm_backend.service;

import com.isscrm.isscrm_backend.model.Address;
import com.isscrm.isscrm_backend.repository.AddressRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AddressService {

    private final AddressRepository repo;

    public AddressService(AddressRepository repo) {
        this.repo = repo;
    }

    public List<Address> getAll() {
        return repo.findAll();
    }

    public Address save(Address address) {
        return repo.save(address);
    }

    public List<Address> search(String keyword) {
        return repo.findByDistrictContainingIgnoreCase(keyword);
    }

    public Address getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Adres bulunamadı"));
    }
}
