


package com.reviewandratings.service;

import com.reviewandratings.dto.ReviewRequestDTO;
import com.reviewandratings.dto.ReviewResponseDTO;
import com.shared_persistence.entity.Review;
import com.shared_persistence.entity.User;
import com.shared_persistence.entity.Room;
import com.shared_persistence.repo.ReviewRepository;
import com.shared_persistence.repo.UserRepository;
import com.shared_persistence.repo.RoomRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    public ReviewResponseDTO addReview(@Valid  ReviewRequestDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        Review review = new Review();
        review.setUser(user);
        review.setRoom(room);
        review.setComment(dto.getComment());
        review.setRating(dto.getRating());

        Review savedReview = reviewRepository.save(review);

        return ReviewResponseDTO.builder()
                .id(savedReview.getReviewId())
                .comment(savedReview.getComment())
                .rating(savedReview.getRating())
                .userId(user.getUserId())
                .roomId(room.getRoomId())
                .createdAt(savedReview.getCreatedAt())
                .build();
    }

    public List<ReviewResponseDTO> getReviewsByUser(Long userId) {
        return reviewRepository.findByUserUserId(userId).stream()
                .map(r -> ReviewResponseDTO.builder()
                        .id(r.getReviewId())
                        .comment(r.getComment())
                        .rating(r.getRating())
                        .userId(r.getUser().getUserId())
                        .roomId(r.getRoom().getRoomId())
                        .createdAt(r.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    public List<ReviewResponseDTO> getReviewsByRoom(Long roomId) {
        return reviewRepository.findByRoomRoomId(roomId).stream()
                .map(r -> ReviewResponseDTO.builder()
                        .id(r.getReviewId())
                        .comment(r.getComment())
                        .rating(r.getRating())
                        .userId(r.getUser().getUserId())
                        .roomId(r.getRoom().getRoomId())
                        .createdAt(r.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }


    public List<ReviewResponseDTO> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(r -> ReviewResponseDTO.builder()
                        .id(r.getReviewId())
                        .comment(r.getComment())
                        .rating(r.getRating())
                        .userId(r.getUser().getUserId())
                        .roomId(r.getRoom().getRoomId())
                        .createdAt(r.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }


    public ReviewResponseDTO updateReview(Long reviewId, ReviewRequestDTO dto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NoSuchElementException("Review not found with ID " + reviewId));

        review.setRating(dto.getRating());
        review.setComment(dto.getComment());

        Review updated = reviewRepository.save(review);

        ReviewResponseDTO response = new ReviewResponseDTO();
        response.setId(updated.getReviewId());
        response.setUserId(updated.getUser().getUserId());
        response.setRoomId(updated.getRoom().getRoomId());
        response.setRating(updated.getRating());
        response.setComment(updated.getComment());
        response.setCreatedAt(updated.getCreatedAt());

        return response;
    }

    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NoSuchElementException("Review not found"));
        reviewRepository.delete(review);
    }


}
