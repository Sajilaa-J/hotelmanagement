package com.reviewandratings.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ReviewRequestDTO {
    private Long userId;
    private Long roomId;
    private String comment;

    private int rating;
}
