package com.isscrm.isscrm_backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ISSCRM Backend API")
                        .version("1.0.0")
                        .description("Backend API Documentation for ISSCRM System")
                        .contact(new Contact()
                                .name("Ahmet Dumanlı")
                                .email("ahmet@isscrm.com")
                                .url("https://futbolla.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }
}
