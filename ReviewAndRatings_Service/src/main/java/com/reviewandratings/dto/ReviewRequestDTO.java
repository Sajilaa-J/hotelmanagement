package com.reviewandratings.dto;

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
