package com.booking_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {
        "com.shared_persistence",
        "com.booking_service"
})
@EnableJpaRepositories(basePackages = {"com.booking_service.repo", "com.shared_persistence.repo"})
@EntityScan(basePackages = "com.shared_persistence.entity")
@EnableFeignClients(basePackages = "com.booking_service.client")
@EnableScheduling
@EnableAsync
public class BookingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookingServiceApplication.class, args);
    }

}
