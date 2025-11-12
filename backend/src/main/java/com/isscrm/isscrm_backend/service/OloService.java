package com.isscrm.isscrm_backend.service;

import com.isscrm.isscrm_backend.model.OloResult;
import com.isscrm.isscrm_backend.repository.OloResultRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class OloService {

    private final OloResultRepository repo;

    public OloService(OloResultRepository repo) {
        this.repo = repo;
    }

    /**
     * Adres koduna göre altyapı sorgular (şimdilik mock veri)
     */
    public OloResult checkAddress(String addressCode) {
        Optional<OloResult> cached = repo.findByAddressCode(addressCode);

        if (cached.isPresent()) {
            return cached.get(); // cache'den döner
        }

        // Mock rastgele sonuç üretimi
        Random rnd = new Random();
        OloResult result = new OloResult();
        result.setAddressCode(addressCode);
        result.setFiberAvailable(rnd.nextBoolean());
        result.setVdslAvailable(true);
        result.setAdslAvailable(true);

        if (result.isFiberAvailable()) result.setMaxSpeed("100 Mbps");
        else if (result.isVdslAvailable()) result.setMaxSpeed("35 Mbps");
        else result.setMaxSpeed("16 Mbps");

        repo.save(result);
        return result;
    }
}
