package com.room_service.controller;
import com.room_service.assembler.RoomModelAssembler;
import com.room_service.dto.RoomRequestDTO;
import com.room_service.dto.RoomResponseDTO;
import com.room_service.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

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
    public ResponseEntity<?> updateRoom(@PathVariable Long id,
                                        @RequestBody RoomRequestDTO roomRequest) {
        try {
            RoomResponseDTO updatedRoom = roomService.updateRoom(id, roomRequest);
            return ResponseEntity.ok(roomAssembler.toModel(updatedRoom));
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest()
                    .body("Error: Room with ID " + id + " not found.");
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Error updating room: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable Long id) {
        try {
            roomService.deleteRoom(id);
            return ResponseEntity.ok("Room deleted successfully!");
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest()
                    .body("Error: Room with ID " + id + " not found.");
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Error deleting room: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<CollectionModel<EntityModel<RoomResponseDTO>>> getAllRooms() {
        List<RoomResponseDTO> rooms = roomService.getAllRooms();
        return ResponseEntity.ok(roomAssembler.toCollectionModel(rooms, AdminRoomController.class, "getAllRooms"));
    }

    @GetMapping("/available")
    public ResponseEntity<CollectionModel<EntityModel<RoomResponseDTO>>> getAvailableRooms() {
        List<RoomResponseDTO> rooms = roomService.getAvailableRooms();
        return ResponseEntity.ok(roomAssembler.toCollectionModel(rooms, AdminRoomController.class, "getAvailableRooms"));
    }
//    @PutMapping("/{roomId}/status")
//    public ResponseEntity<?> updateRoomStatus(@PathVariable Long roomId, @RequestParam String status) {
//        roomService.updateRoomStatus(roomId, status);
//        return ResponseEntity.ok("Room status updated to " + status);
//    }

}
