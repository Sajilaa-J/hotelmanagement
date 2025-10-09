package com.booking_service.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class BookingResponseDTO {
    private Long id;
    private Long roomId;
    private Long userId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Double totalAmount;
    private String status;
}
