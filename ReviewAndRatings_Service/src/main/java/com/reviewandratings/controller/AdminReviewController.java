

//package com.reviewandratings.controller;
//
//import com.reviewandratings.assembler.ReviewModelAssembler;
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
//@RequestMapping("/api/admin/reviews")
//@RequiredArgsConstructor
//public class AdminReviewController {
//
//    private final ReviewService reviewService;
//    private final ReviewModelAssembler reviewAssembler;
//
//    @GetMapping
//    public ResponseEntity<CollectionModel<EntityModel<ReviewResponseDTO>>> getAllReviews() {
//        List<ReviewResponseDTO> reviews = reviewService.getAllReviews();
//        return ResponseEntity.ok(reviewAssembler.toCollectionModel(reviews, AdminReviewController.class, "getAllReviews"));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<EntityModel<ReviewResponseDTO>> deleteReview(@PathVariable Long id) {
//        ReviewResponseDTO deletedReview = reviewService.deleteReview(id);
//        return ResponseEntity.ok(reviewAssembler.toModel(deletedReview));
//    }
//}
package com.reviewandratings.controller;

import com.reviewandratings.assembler.ReviewModelAssembler;
import com.reviewandratings.dto.ReviewResponseDTO;
import com.reviewandratings.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/admin/reviews")
@RequiredArgsConstructor
public class AdminReviewController {

    private final ReviewService reviewService;
    private final ReviewModelAssembler reviewAssembler;

    @GetMapping
    public ResponseEntity<?> getAllReviews() {
        try {
            List<ReviewResponseDTO> reviews = reviewService.getAllReviews();
            if (reviews.isEmpty()) {
                return ResponseEntity.ok("No reviews found");
            }
            return ResponseEntity.ok(
                    reviewAssembler.toCollectionModel(reviews, AdminReviewController.class, "getAllReviews")
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving reviews: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id) {
        try {
            reviewService.deleteReview(id);
            return ResponseEntity.ok("Review with ID " + id + " deleted successfully.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body("Error: Review with ID " + id + " not found");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting review: " + e.getMessage());
        }
    }

}

