package com.isscrm.isscrm_backend.mock;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/mock/mikrotik")
public class FakeMikroTikServer {

    private static final String MOCK_TOKEN = "fake-mikrotik-token";

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        if ("admin".equals(username) && "1234".equals(password)) {
            Map<String, String> response = new HashMap<>();
            response.put("token", MOCK_TOKEN);
            System.out.println("✅ [MOCK] MikroTik login simulated.");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
    }

    @PostMapping("/ppp/profile/add")
    public ResponseEntity<Map<String, String>> addPPPProfile(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> body) {

        if (authHeader == null || !authHeader.equals("Bearer " + MOCK_TOKEN)) {
            System.out.println("⚠️ [MOCK] Unauthorized access to /ppp/profile/add");
            return ResponseEntity.status(403).body(Map.of("error", "Unauthorized"));
        }

        String name = body.get("name");
        String rateLimit = body.get("rate-limit");

        System.out.println("✅ [MOCK] PPP Profile created: " + name + " | Rate: " + rateLimit);
        return ResponseEntity.ok(Map.of("status", "success", "profileName", name));
    }
}
