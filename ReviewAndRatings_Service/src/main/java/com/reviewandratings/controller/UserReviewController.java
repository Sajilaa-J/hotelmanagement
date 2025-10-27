

//package com.reviewandratings.controller;
//
//import com.reviewandratings.assembler.ReviewModelAssembler;
//import com.reviewandratings.dto.ReviewRequestDTO;
//import com.reviewandratings.dto.ReviewResponseDTO;
//import com.reviewandratings.service.ReviewService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.hateoas.CollectionModel;
//import org.springframework.hateoas.EntityModel;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/user/reviews")
//@RequiredArgsConstructor
//public class UserReviewController {
//
//    private final ReviewService reviewService;
//    private final ReviewModelAssembler reviewAssembler;
//
//    @PostMapping
//    public ResponseEntity<EntityModel<ReviewResponseDTO>> addReview(@RequestBody ReviewRequestDTO dto) {
//        ReviewResponseDTO review = reviewService.addReview(dto);
//        return ResponseEntity.ok(reviewAssembler.toModel(review));
//    }
//
//    @GetMapping("/room/{roomId}")
//    public ResponseEntity<CollectionModel<EntityModel<ReviewResponseDTO>>> getReviewsByRoom(@PathVariable Long roomId) {
//        List<ReviewResponseDTO> reviews = reviewService.getReviewsByRoom(roomId);
//        return ResponseEntity.ok(reviewAssembler.toCollectionModel(reviews, UserReviewController.class, "getReviewsByRoom"));
//    }
//}
package com.reviewandratings.controller;

import com.reviewandratings.assembler.ReviewModelAssembler;
import com.reviewandratings.dto.ReviewRequestDTO;
import com.reviewandratings.dto.ReviewResponseDTO;
import com.reviewandratings.kafka.ReviewProducer;
import com.reviewandratings.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/user/reviews")
@RequiredArgsConstructor
public class UserReviewController {

    private final ReviewService reviewService;
    private final ReviewModelAssembler reviewAssembler;
    private final ReviewProducer reviewProducer;

    @PostMapping
    public ResponseEntity<?> addReview(@RequestBody ReviewRequestDTO dto) {
        try {
            ReviewResponseDTO review = reviewService.addReview(dto);
            reviewProducer.sendReviewEvent(review);
            return ResponseEntity.ok(reviewAssembler.toModel(review));
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error adding review: " + e.getMessage());
        }
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<?> getReviewsByRoom(@PathVariable Long roomId) {
        try {
            List<ReviewResponseDTO> reviews = reviewService.getReviewsByRoom(roomId);
            if (reviews.isEmpty()) {
                return ResponseEntity.ok("No reviews found for room with ID " + roomId);
            }
            return ResponseEntity.ok(
                    reviewAssembler.toCollectionModel(reviews, UserReviewController.class, "getReviewsByRoom")
            );
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body("Error: Room with ID " + roomId + " not found");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving reviews: " + e.getMessage());
        }
    }


//    @PutMapping("/{reviewId}")
//    public ResponseEntity<?> updateReview(@PathVariable Long reviewId, @RequestBody ReviewRequestDTO dto) {
//        try {
//            ReviewResponseDTO updatedReview = reviewService.updateReview(reviewId, dto);
//            return ResponseEntity.ok(reviewAssembler.toModel(updatedReview));
//        } catch (NoSuchElementException | IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Error updating review: " + e.getMessage());
//        }
//    }


    @PutMapping("/{reviewId}")
    public ResponseEntity<?> updateReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewRequestDTO dto) {
        try {
            ReviewResponseDTO updatedReview = reviewService.updateReview(reviewId, dto);

            if (updatedReview == null) {
                return ResponseEntity.status(404)
                        .body("Error: Review not found with ID " + reviewId);
            }
            return ResponseEntity.ok(reviewAssembler.toModel(updatedReview));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404)
                    .body("Error: Review not found with ID " + reviewId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Error updating review: " + e.getMessage());
        }
    }


    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId) {
        try {
            reviewService.deleteReview(reviewId);
            return ResponseEntity.ok("Review with ID " + reviewId + " deleted successfully.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body("Error: Review with ID " + reviewId + " not found");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting review: " + e.getMessage());
        }
    }
}

