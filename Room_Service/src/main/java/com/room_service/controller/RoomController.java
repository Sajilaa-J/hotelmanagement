package com.room_service.controller;

import com.room_service.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RoomController {
    @Autowired
    private RoomService roomService;

    @PutMapping("/api/internal/rooms/{roomId}/status")
    public ResponseEntity<?> updateRoomStatus(
            @PathVariable Long roomId,
            @RequestParam String status) {

        String updatedBy = "SYSTEM";

        roomService.updateRoomStatus(roomId, status, updatedBy);

        return ResponseEntity.ok("Room status updated to " + status);
    }
}