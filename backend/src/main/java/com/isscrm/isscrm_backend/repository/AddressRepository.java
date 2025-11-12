package com.isscrm.isscrm_backend.repository;

import com.isscrm.isscrm_backend.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByDistrictContainingIgnoreCase(String district);
    List<Address> findByProvinceContainingIgnoreCase(String province);
}
