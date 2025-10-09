//package com.reviewandratings.controller;
//import com.reviewandratings.dto.ReviewRequestDTO;
//import com.reviewandratings.dto.ReviewResponseDTO;
//import com.reviewandratings.service.ReviewService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/user/reviews")
//public class UserReviewController {
//
//    @Autowired
//    private ReviewService reviewService;
//
//    @PostMapping
//    public ReviewResponseDTO addReview(@RequestBody ReviewRequestDTO dto) {
//        return reviewService.addReview(dto);
//    }
//
//    @GetMapping("/room/{roomId}")
//    public List<ReviewResponseDTO> getReviewsByRoom(@PathVariable Long roomId) {
//        return reviewService.getReviewsByRoom(roomId);
//    }
//}

package com.reviewandratings.controller;

import com.reviewandratings.assembler.ReviewModelAssembler;
import com.reviewandratings.dto.ReviewRequestDTO;
import com.reviewandratings.dto.ReviewResponseDTO;
import com.reviewandratings.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/reviews")
@RequiredArgsConstructor
public class UserReviewController {

    private final ReviewService reviewService;
    private final ReviewModelAssembler reviewAssembler;

    @PostMapping
    public ResponseEntity<EntityModel<ReviewResponseDTO>> addReview(@RequestBody ReviewRequestDTO dto) {
        ReviewResponseDTO review = reviewService.addReview(dto);
        return ResponseEntity.ok(reviewAssembler.toModel(review));
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<CollectionModel<EntityModel<ReviewResponseDTO>>> getReviewsByRoom(@PathVariable Long roomId) {
        List<ReviewResponseDTO> reviews = reviewService.getReviewsByRoom(roomId);
        return ResponseEntity.ok(reviewAssembler.toCollectionModel(reviews, UserReviewController.class, "getReviewsByRoom"));
    }
}

