package com.isscrm.isscrm_backend.repository;

import com.isscrm.isscrm_backend.model.Dealer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DealerRepository extends JpaRepository<Dealer, Long> {
    Dealer findByDealerCode(String dealerCode);
}
