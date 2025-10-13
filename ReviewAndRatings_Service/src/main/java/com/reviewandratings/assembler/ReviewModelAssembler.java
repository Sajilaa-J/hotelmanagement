package com.reviewandratings.assembler;

import com.reviewandratings.controller.AdminReviewController;
import com.reviewandratings.controller.UserReviewController;
import com.reviewandratings.dto.ReviewResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ReviewModelAssembler implements RepresentationModelAssembler<ReviewResponseDTO, EntityModel<ReviewResponseDTO>> {

    @Override
    public EntityModel<ReviewResponseDTO> toModel(ReviewResponseDTO review) {
        EntityModel<ReviewResponseDTO> model = EntityModel.of(review);
        model.add(linkTo(methodOn(AdminReviewController.class)
                .deleteReview(review.getId())).withRel("delete-review"));
        model.add(linkTo(methodOn(AdminReviewController.class)
                .getAllReviews()).withRel("all-reviews"));

        model.add(linkTo(methodOn(UserReviewController.class)
                .getReviewsByRoom(review.getRoomId())).withRel("room-reviews"));
        return model;
    }

    public CollectionModel<EntityModel<ReviewResponseDTO>> toCollectionModel(List<ReviewResponseDTO> reviews, Class<?> controllerClass, String selfMethod) {
        List<EntityModel<ReviewResponseDTO>> reviewModels = reviews.stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<ReviewResponseDTO>> collection = CollectionModel.of(reviewModels);

        try {
            collection.add(linkTo(controllerClass.getMethod(selfMethod)).withSelfRel());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return collection;
    }
}
