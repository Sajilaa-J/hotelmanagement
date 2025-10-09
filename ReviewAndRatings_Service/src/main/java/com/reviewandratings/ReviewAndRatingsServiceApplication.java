package com.reviewandratings;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
        "com.shared_persistence",
        "com.reviewandratings"
})
@EnableJpaRepositories(basePackages = { "com.shared_persistence.repo"})
@EntityScan(basePackages = "com.shared_persistence.entity")
public class ReviewAndRatingsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReviewAndRatingsServiceApplication.class, args);
    }

}
