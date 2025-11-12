package com.isscrm.isscrm_backend.integration;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.HashMap;
import java.util.Map;

@Component
public class MikroTikClient {

    private final RestTemplate restTemplate = new RestTemplate();

    private final String baseUrl = "http://localhost:8080/mock/mikrotik"; // MikroTik API endpoint
    private final String username = "admin";
    private final String password = "1234";

    private String authToken;

    public void login() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", username);
        credentials.put("password", password);

        ResponseEntity<Map> response = restTemplate.postForEntity(baseUrl + "/login", credentials, Map.class);
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            authToken = (String) response.getBody().get("token");
            System.out.println("✅ MikroTik login successful.");
        } else {
            System.out.println("❌ MikroTik login failed: " + response.getStatusCode());
        }
    }

    public void createPPPProfile(String name, String rateLimit) {
        if (authToken == null) login();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("name", name);
        body.put("rate-limit", rateLimit);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(baseUrl + "/ppp/profile/add", request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("✅ PPP Profile created in MikroTik: " + name);
        } else {
            System.out.println("❌ Failed to create PPP Profile: " + response.getStatusCode());
        }
    }
}
