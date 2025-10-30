package com.room_service.controller;

import com.room_service.assembler.RoomModelAssembler;
import com.room_service.dto.RoomResponseDTO;
import com.room_service.service.RoomService;
import com.shared_persistence.entity.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user/rooms")
public class UserRoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomModelAssembler roomAssembler;

    @GetMapping("/search")
    public ResponseEntity<List<Room>> searchRooms(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String status)
        {

        List<Room> rooms = roomService.searchRooms(type, minPrice, maxPrice, status);
        return ResponseEntity.ok(rooms);
    }

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

