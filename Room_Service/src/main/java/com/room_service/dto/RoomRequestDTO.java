package com.room_service.dto;

import lombok.Data;

@Data
public class RoomRequestDTO {

    private String roomNumber;
    private String type;              // e.g., Single, Double, Suite
    private double price;
    private String availabilityStatus; // Available / Booked
    private String description;
}
