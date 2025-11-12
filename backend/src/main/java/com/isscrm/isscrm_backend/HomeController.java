package com.isscrm.isscrm_backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "ISSCRM Backend API is running 🚀";
    }

    @GetMapping("/api/health")
    public String health() {
        return "{\"status\":\"OK\"}";
    }
}
