package com.booking_service.security;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {

    @Value("${ADMIN_TOKEN}")
    private String adminToken;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return (RequestTemplate template) -> {

            if (template.url().contains("/internal/")) {
                template.header("Authorization", "Bearer " + adminToken);
            }
        };
    }
}
