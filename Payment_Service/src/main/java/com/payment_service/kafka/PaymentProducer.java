package com.payment_service.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "payment-topic";

    public void sendPaymentMessage(String message) {
        kafkaTemplate.send(TOPIC, message);
        System.out.println("📤 Payment message sent to Kafka: " + message);
    }
}
