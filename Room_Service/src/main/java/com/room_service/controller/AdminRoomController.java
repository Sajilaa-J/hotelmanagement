package com.room_service.controller;
//import com.room_service.dto.RoomRequestDTO;
//import com.room_service.dto.RoomResponseDTO;
//import com.room_service.service.RoomService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/admin/rooms")
//public class AdminRoomController {
//
//    @Autowired
//    private RoomService roomService;
//
//
//    @PostMapping("/add")
//    public ResponseEntity<RoomResponseDTO> addRoom(@RequestBody RoomRequestDTO roomRequest) {
//        RoomResponseDTO room = roomService.addRoom(roomRequest);
//        return ResponseEntity.ok(room);
//    }
//
//
//    @PutMapping("/update/{id}")
//    public ResponseEntity<RoomResponseDTO> updateRoom(@PathVariable Long id,
//                                                      @RequestBody RoomRequestDTO roomRequest) {
//        RoomResponseDTO updatedRoom = roomService.updateRoom(id, roomRequest);
//        return ResponseEntity.ok(updatedRoom);
//    }
//
//
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<String> deleteRoom(@PathVariable Long id) {
//        roomService.deleteRoom(id);
//        return ResponseEntity.ok("Room deleted successfully!");
//    }
//
//
//    @GetMapping("/all")
//    public ResponseEntity<List<RoomResponseDTO>> getAllRooms() {
//        List<RoomResponseDTO> rooms = roomService.getAllRooms();
//        return ResponseEntity.ok(rooms);
//    }
//
//    @GetMapping("/available")
//    public ResponseEntity<List<RoomResponseDTO>> getAvailableRooms() {
//        List<RoomResponseDTO> rooms = roomService.getAvailableRooms();
//        return ResponseEntity.ok(rooms);
//    }
//}


import com.room_service.assembler.RoomModelAssembler;
import com.room_service.dto.RoomRequestDTO;
import com.room_service.dto.RoomResponseDTO;
import com.room_service.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/admin/rooms")
public class AdminRoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomModelAssembler roomAssembler;

    @PostMapping("/add")
    public ResponseEntity<EntityModel<RoomResponseDTO>> addRoom(@RequestBody RoomRequestDTO roomRequest) {
        RoomResponseDTO room = roomService.addRoom(roomRequest);
        return ResponseEntity.ok(roomAssembler.toModel(room));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EntityModel<RoomResponseDTO>> updateRoom(@PathVariable Long id,
                                                                   @RequestBody RoomRequestDTO roomRequest) {
        RoomResponseDTO updatedRoom = roomService.updateRoom(id, roomRequest);
        return ResponseEntity.ok(roomAssembler.toModel(updatedRoom));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.ok("Room deleted successfully!");
    }

    @GetMapping("/all")
    public ResponseEntity<CollectionModel<EntityModel<RoomResponseDTO>>> getAllRooms() {
        List<RoomResponseDTO> rooms = roomService.getAllRooms();
        // Just call the assembler, no manual links
        return ResponseEntity.ok(roomAssembler.toCollectionModel(rooms, AdminRoomController.class, "getAllRooms"));
    }

    @GetMapping("/available")
    public ResponseEntity<CollectionModel<EntityModel<RoomResponseDTO>>> getAvailableRooms() {
        List<RoomResponseDTO> rooms = roomService.getAvailableRooms();
        return ResponseEntity.ok(roomAssembler.toCollectionModel(rooms, AdminRoomController.class, "getAvailableRooms"));
    }

}
