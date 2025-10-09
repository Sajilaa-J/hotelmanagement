package com.room_service.service;
import com.room_service.dto.RoomRequestDTO;
import com.room_service.dto.RoomResponseDTO;
import com.room_service.repo.RoomRepository;
import com.shared_persistence.entity.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    // Convert entity to DTO
    private RoomResponseDTO convertToDTO(Room room) {
        RoomResponseDTO dto = new RoomResponseDTO();
        dto.setRoomId(room.getRoomId());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setType(room.getType());
        dto.setPrice(room.getPrice());
        dto.setAvailabilityStatus(room.getAvailabilityStatus());
        dto.setDescription(room.getDescription());
        return dto;
    }

    // Add a room
    public RoomResponseDTO addRoom(RoomRequestDTO request) {
        Room room = new Room();
        room.setRoomNumber(request.getRoomNumber());
        room.setType(request.getType());
        room.setPrice(request.getPrice());
        room.setAvailabilityStatus(request.getAvailabilityStatus());
        room.setDescription(request.getDescription());
        Room saved = roomRepository.save(room);
        return convertToDTO(saved);
    }

    // Update room
    public RoomResponseDTO updateRoom(Long id, RoomRequestDTO request) {
        Room existing = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (request.getRoomNumber() != null) existing.setRoomNumber(request.getRoomNumber());
        if (request.getType() != null) existing.setType(request.getType());
        if (request.getPrice() != 0) existing.setPrice(request.getPrice());
        if (request.getAvailabilityStatus() != null) existing.setAvailabilityStatus(request.getAvailabilityStatus());
        if (request.getDescription() != null) existing.setDescription(request.getDescription());

        Room updated = roomRepository.save(existing);
        return convertToDTO(updated);
    }

    // Delete room
    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }

    // Get all rooms
    public List<RoomResponseDTO> getAllRooms() {
        return roomRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get available rooms
    public List<RoomResponseDTO> getAvailableRooms() {
        return roomRepository.findByAvailabilityStatus("Available")
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}
