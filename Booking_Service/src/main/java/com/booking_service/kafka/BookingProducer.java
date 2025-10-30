package com.booking_service.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class BookingProducer {



    private static final String TOPIC = "room-booking-topic";
    private final KafkaTemplate<String, String> kafkaTemplate;

    public BookingProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendBookingEvent(Long roomId, Long userId) {
        String message = "RoomBooked:" + roomId + (userId != null ? ",User:" + userId : "");
        kafkaTemplate.send(TOPIC, message);
        System.out.println("📤 Sent Kafka message: " + message);
    }

    public void sendRoomReleasedEvent(Long roomId) {
        String message = "RoomReleased:" + roomId;
        kafkaTemplate.send(TOPIC, message);
        System.out.println("♻️ Sent Kafka release message: " + message);
    }

}
