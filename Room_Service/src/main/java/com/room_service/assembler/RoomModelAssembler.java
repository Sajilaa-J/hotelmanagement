package com.room_service.assembler;

import com.room_service.controller.AdminRoomController;
import com.room_service.controller.UserRoomController;
import com.room_service.dto.RoomResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoomModelAssembler implements RepresentationModelAssembler<RoomResponseDTO, EntityModel<RoomResponseDTO>> {

    @Override
    public EntityModel<RoomResponseDTO> toModel(RoomResponseDTO room) {
        EntityModel<RoomResponseDTO> model = EntityModel.of(room);

        model.add(linkTo(methodOn(AdminRoomController.class).updateRoom(room.getRoomId(), null)).withRel("update-room"));
        model.add(linkTo(methodOn(AdminRoomController.class).deleteRoom(room.getRoomId())).withRel("delete-room"));
        model.add(linkTo(methodOn(UserRoomController.class).getAllRooms()).withRel("all-rooms"));
        model.add(linkTo(methodOn(UserRoomController.class).getAvailableRooms()).withRel("available-rooms"));

        return model;
    }

    public CollectionModel<EntityModel<RoomResponseDTO>> toCollectionModel(List<RoomResponseDTO> rooms, Class<?> controllerClass, String selfMethod) {
        List<EntityModel<RoomResponseDTO>> roomModels = rooms.stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<RoomResponseDTO>> collection = CollectionModel.of(roomModels);

        try {
            collection.add(linkTo(controllerClass.getMethod(selfMethod)).withSelfRel());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return collection;
    }
}

