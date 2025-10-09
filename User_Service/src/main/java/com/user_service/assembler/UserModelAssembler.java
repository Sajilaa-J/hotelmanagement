package com.user_service.assembler;

import com.shared_persistence.entity.User;
import com.user_service.controller.AuthController;
import com.user_service.dto.AuthResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<AuthResponse, EntityModel<AuthResponse>> {

    @Override
    public EntityModel<AuthResponse> toModel(AuthResponse response) {
        EntityModel<AuthResponse> model = EntityModel.of(response);
        model.add(linkTo(methodOn(AuthController.class).register(null)).withRel("register"));
        model.add(linkTo(methodOn(AuthController.class).login(null)).withRel("login"));
        return model;
    }

}
