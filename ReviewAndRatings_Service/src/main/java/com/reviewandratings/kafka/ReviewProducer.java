package com.reviewandratings.kafka;

import com.reviewandratings.dto.ReviewResponseDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReviewProducer {

    private final KafkaTemplate<String, ReviewResponseDTO> kafkaTemplate;

    public ReviewProducer(KafkaTemplate<String, ReviewResponseDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendReviewEvent(ReviewResponseDTO review) {
        kafkaTemplate.send("review-topic", review);
        System.out.println("📤 Sent Kafka Message: " + review);
    }
}

