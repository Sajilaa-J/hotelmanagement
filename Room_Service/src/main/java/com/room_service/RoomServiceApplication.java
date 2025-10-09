package com.room_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

//
//@SpringBootApplication
//@EntityScan(basePackages = {"com.shared_persistence.entity"})
//@EnableJpaRepositories(basePackages = {"com.room_service"})
@SpringBootApplication(scanBasePackages = {
        "com.shared_persistence",
        "com.room_service"
})
@EntityScan(basePackages = "com.shared_persistence.entity")
@EnableFeignClients(basePackages = "com.room_service.client")
public class RoomServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoomServiceApplication.class, args);
    }

}
