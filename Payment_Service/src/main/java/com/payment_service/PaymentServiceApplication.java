package com.payment_service;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.shared_persistence.entity"})
@EnableJpaRepositories(basePackages = {"com.shared_persistence"})
@ComponentScan(basePackages = {"com.payment_service", "com.shared_persistence"})
    public class PaymentServiceApplication {

        public static void main(String[] args) {
            SpringApplication.run(PaymentServiceApplication.class, args);
        }

    }
