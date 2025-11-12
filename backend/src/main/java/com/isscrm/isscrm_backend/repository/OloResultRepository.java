package com.isscrm.isscrm_backend.repository;

import com.isscrm.isscrm_backend.model.OloResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OloResultRepository extends JpaRepository<OloResult, Long> {
    Optional<OloResult> findByAddressCode(String addressCode);
}
