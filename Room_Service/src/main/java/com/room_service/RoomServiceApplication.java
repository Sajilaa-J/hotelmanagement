package com.room_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//
//@SpringBootApplication
//@EntityScan(basePackages = {"com.shared_persistence.entity"})
//@EnableJpaRepositories(basePackages = {"com.room_service"})
@SpringBootApplication(scanBasePackages = {
        "com.shared_persistence",
        "com.room_service"
})
@EntityScan(basePackages = "com.shared_persistence.entity")
@EnableJpaRepositories(basePackages = { "com.shared_persistence.repo"})
//@EnableJpaRepositories(basePackages = "com.shared_persistence.repo")
@EnableFeignClients
public class RoomServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoomServiceApplication.class, args);
    }

}
