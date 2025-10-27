//package com.payment_service.kafka;
//
//
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//@Service
//public class PaymentConsumer {
//
//    @KafkaListener(topics = "payment-topic", groupId = "payment-group")
//    public void consume(PaymentMessage message) {
//        System.out.println("📥 Received message from Kafka: " + message);
//    }
//}
