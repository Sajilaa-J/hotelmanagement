package com.room_service.controller;

import com.room_service.assembler.RoomModelAssembler;
import com.room_service.dto.RoomResponseDTO;
import com.room_service.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/rooms")
public class UserRoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomModelAssembler roomAssembler;

    @GetMapping("/available")
    public ResponseEntity<CollectionModel<EntityModel<RoomResponseDTO>>> getAvailableRooms() {
        return ResponseEntity.ok(
                roomAssembler.toCollectionModel(roomService.getAvailableRooms())
        );
    }

    @GetMapping("/all")
    public ResponseEntity<CollectionModel<EntityModel<RoomResponseDTO>>> getAllRooms() {
        return ResponseEntity.ok(
                roomAssembler.toCollectionModel(roomService.getAllRooms())
        );
    }
}

