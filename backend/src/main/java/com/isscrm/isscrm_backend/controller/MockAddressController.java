package com.isscrm.isscrm_backend.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/mock/address")
@CrossOrigin(origins = "*")
public class MockAddressController {

    @GetMapping("/lookup")
    public Map<String, Object> lookup(@RequestParam String query) {
        Map<String, Object> response = new HashMap<>();
        response.put("addressCode", "06537933249");
        response.put("province", "Ankara");
        response.put("district", "Çankaya");
        response.put("neighborhood", "Mutlukent");
        response.put("street", "Atatürk Bulvarı");
        response.put("buildingNo", "123");
        response.put("postalCode", "06530");
        response.put("fullAddress", "Ankara Çankaya Mutlukent Mah. Atatürk Blv. No:123");
        return response;
    }
}
