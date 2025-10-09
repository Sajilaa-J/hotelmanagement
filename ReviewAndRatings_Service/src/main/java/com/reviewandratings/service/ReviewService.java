//package com.reviewandratings.service;
//
//import com.reviewandratings.dto.ReviewRequestDTO;
//import com.reviewandratings.dto.ReviewResponseDTO;
//import com.shared_persistence.entity.Review;
//import com.shared_persistence.entity.User;
//import com.shared_persistence.entity.Room;
//import com.shared_persistence.repo.ReviewRepository;
//import com.shared_persistence.repo.UserRepository;
//import com.shared_persistence.repo.RoomRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class ReviewService {
//
//    @Autowired
//    private ReviewRepository reviewRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private RoomRepository roomRepository;
//
//    // Add a new review
//    public ReviewResponseDTO addReview(ReviewRequestDTO dto) {
//
//        User user = userRepository.findById(dto.getUserId())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Room room = roomRepository.findById(dto.getRoomId())
//                .orElseThrow(() -> new RuntimeException("Room not found"));
//
//        Review review = new Review();
//        review.setUser(user);
//        review.setRoom(room);
//        review.setComment(dto.getComment());
//        review.setRating(dto.getRating());
//
//        Review savedReview = reviewRepository.save(review);
//
//        return ReviewResponseDTO.builder()
//                .id(savedReview.getReviewId())
//                .comment(savedReview.getComment())
//                .rating(savedReview.getRating())
//                .userId(user.getUserId())
//                .roomId(room.getRoomId())
//                .createdAt(savedReview.getCreatedAt())
//                .build();
//    }
//
//    // Get reviews by room ID
//    public List<ReviewResponseDTO> getReviewsByRoom(Long roomId) {
//        return reviewRepository.findByRoomId(roomId).stream()
//                .map(r -> ReviewResponseDTO.builder()
//                        .id(r.getReviewId())
//                        .comment(r.getComment())
//                        .rating(r.getRating())
//                        .userId(r.getUser().getUserId())
//                        .roomId(r.getRoom().getRoomId())
//                        .createdAt(r.getCreatedAt())
//                        .build())
//                .collect(Collectors.toList());
//    }
//}
//package com.reviewandratings.service;
//
//import com.reviewandratings.dto.ReviewRequestDTO;
//import com.reviewandratings.dto.ReviewResponseDTO;
//import com.shared_persistence.entity.Review;
//import com.shared_persistence.entity.User;
//import com.shared_persistence.entity.Room;
//import com.shared_persistence.repo.ReviewRepository;
//import com.shared_persistence.repo.UserRepository;
//import com.shared_persistence.repo.RoomRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class ReviewService {
//
//    @Autowired
//    private ReviewRepository reviewRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private RoomRepository roomRepository;
//
//    // User adds a review
//    public ReviewResponseDTO addReview(ReviewRequestDTO dto) {
//        User user = userRepository.findById(dto.getUserId())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Room room = roomRepository.findById(dto.getRoomId())
//                .orElseThrow(() -> new RuntimeException("Room not found"));
//
//        Review review = new Review();
//        review.setUser(user);
//        review.setRoom(room);
//        review.setComment(dto.getComment());
//        review.setRating(dto.getRating());
//
//        Review savedReview = reviewRepository.save(review);
//
//        return ReviewResponseDTO.builder()
//                .id(savedReview.getReviewId())
//                .comment(savedReview.getComment())
//                .rating(savedReview.getRating())
//                .userId(user.getUserId())
//                .roomId(room.getRoomId())
//                .createdAt(savedReview.getCreatedAt())
//                .build();
//    }
//
//    // Get reviews by room ID (for users)
//    public List<ReviewResponseDTO> getReviewsByRoom(Long roomId) {
//        return reviewRepository.findById(roomId).stream()
//                .map(r -> ReviewResponseDTO.builder()
//                        .id(r.getReviewId())
//                        .comment(r.getComment())
//                        .rating(r.getRating())
//                        .userId(r.getUser().getUserId())
//                        .roomId(r.getRoom().getRoomId())
//                        .createdAt(r.getCreatedAt())
//                        .build())
//                .collect(Collectors.toList());
//    }
//
//    // Admin: Get all reviews
//    public List<ReviewResponseDTO> getAllReviews() {
//        return reviewRepository.findAll().stream()
//                .map(r -> ReviewResponseDTO.builder()
//                        .id(r.getReviewId())
//                        .comment(r.getComment())
//                        .rating(r.getRating())
//                        .userId(r.getUser().getUserId())
//                        .roomId(r.getRoom().getRoomId())
//                        .createdAt(r.getCreatedAt())
//                        .build())
//                .collect(Collectors.toList());
//    }
//
//    // Admin: Delete a review by ID
////    public void deleteReview(Long id) {
////        Review review = reviewRepository.findById(id)
////                //.orElseThrow(() -> new RuntimeException("Review not found with ID: " + id));
////        reviewRepository.delete(review);
////    }
//}



package com.reviewandratings.service;

import com.reviewandratings.dto.ReviewRequestDTO;
import com.reviewandratings.dto.ReviewResponseDTO;
import com.shared_persistence.entity.Review;
import com.shared_persistence.entity.User;
import com.shared_persistence.entity.Room;
import com.shared_persistence.repo.ReviewRepository;
import com.shared_persistence.repo.UserRepository;
import com.shared_persistence.repo.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    public ReviewResponseDTO addReview(ReviewRequestDTO dto) {
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

    // Get reviews by user ID
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

    // Get reviews by room ID
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

    // Admin: Get all reviews
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

    // Admin: Delete a review by ID
//    public void deleteReview(Long id) {
//        Review review = reviewRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Review not found with ID: " + id));
//        reviewRepository.delete(review);
//    }

    public ReviewResponseDTO deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with ID: " + id));
        reviewRepository.delete(review);

        return ReviewResponseDTO.builder()
                .id(review.getReviewId())
                .comment(review.getComment())
                .rating(review.getRating())
                .userId(review.getUser().getUserId())
                .roomId(review.getRoom().getRoomId())
                .createdAt(review.getCreatedAt())
                .build();
    }

}
