package com.user_service.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public UserProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendUserCreatedEvent(String name, String email) {
        String message = "UserCreated:Name:" + name + ",Email:" + email;
        kafkaTemplate.send("user-topic", message);
        System.out.println("📤 Sent Kafka message: " + message);
    }
}
