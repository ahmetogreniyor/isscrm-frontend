package com.isscrm.isscrm_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class IsscrmBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(IsscrmBackendApplication.class, args);
        System.out.println("✅ ISSCRM Backend is running with Scheduler enabled...");
    }
}
