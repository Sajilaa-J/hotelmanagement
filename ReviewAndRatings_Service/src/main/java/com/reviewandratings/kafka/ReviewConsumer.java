package com.reviewandratings.kafka;
import com.reviewandratings.dto.ReviewResponseDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ReviewConsumer {

    @KafkaListener(topics = "review-topic", groupId = "review-group")
    public void consumeReviewEvent(ReviewResponseDTO review) {
        System.out.println("📥 Received Kafka Message: " + review);
    }
}


