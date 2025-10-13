package com.reviewandratings.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseDTO {
    private Long id;
    private String comment;
    private int rating;
    private Long userId;
    private Long roomId;
    private LocalDateTime createdAt;
}
