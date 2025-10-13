package com.payment_service.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDTO {
    //private Long paymentId;
//    private String paymentStatus;
//    private Double amount;
//    private LocalDateTime paymentDate;

    private Long id;
    private Long userId;
    private Long bookingId;
    private Double amount;
    private String paymentStatus;
    private LocalDateTime paymentDate;

}
