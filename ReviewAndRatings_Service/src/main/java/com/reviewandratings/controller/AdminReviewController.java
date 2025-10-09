//package com.reviewandratings.controller;
//
//import com.reviewandratings.dto.ReviewResponseDTO;
//import com.reviewandratings.service.ReviewService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/admin/reviews")
//public class AdminReviewController {
//
//    @Autowired
//    private ReviewService reviewService;
//
//    @GetMapping
//    public List<ReviewResponseDTO> getAllReviews() {
//        return reviewService.getAllReviews(); // Service should return list of ReviewResponseDTO
//    }
//
//    @DeleteMapping("/{id}")
//    public String deleteReview(@PathVariable Long id) {
//        reviewService.deleteReview(id); // Service handles deletion logic
//        return "Review with ID " + id + " has been deleted.";
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

@RestController
@RequestMapping("/api/admin/reviews")
@RequiredArgsConstructor
public class AdminReviewController {

    private final ReviewService reviewService;
    private final ReviewModelAssembler reviewAssembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ReviewResponseDTO>>> getAllReviews() {
        List<ReviewResponseDTO> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviewAssembler.toCollectionModel(reviews, AdminReviewController.class, "getAllReviews"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EntityModel<ReviewResponseDTO>> deleteReview(@PathVariable Long id) {
        ReviewResponseDTO deletedReview = reviewService.deleteReview(id);
        return ResponseEntity.ok(reviewAssembler.toModel(deletedReview));
    }
}

