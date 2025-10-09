package com.room_service.dto;

import lombok.Data;

@Data
public class RoomResponseDTO {

    private Long roomId;
    private String roomNumber;
    private String type;
    private double price;
    private String availabilityStatus;
    private String description;

}
