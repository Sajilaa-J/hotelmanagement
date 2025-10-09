package com.booking_service.dto;


import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class BookingRequestDTO {
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Long roomId;
    private Long userId;
    private Double totalAmount;
}
