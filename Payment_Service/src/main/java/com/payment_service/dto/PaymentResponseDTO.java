package com.payment_service.dto;

import lombok.*;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDTO {
    //private Long paymentId;
    private String paymentStatus;
    private Double amount;
    private LocalDateTime paymentDate;

}
