package com.user_service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserConsumer {

    @KafkaListener(topics = "user-topic", groupId = "user-service-group")
    public void consume(String message) {
        System.out.println("📥 Received Kafka message: " + message);

        if (message.startsWith("UserCreated:")) {
            String[] parts = message.split(",");
            String name = parts[0].split(":")[2];
            String email = parts[1].split(":")[1];

            System.out.println("🎉 New user created: " + name + " (" + email + ")");
        }
    }
}
