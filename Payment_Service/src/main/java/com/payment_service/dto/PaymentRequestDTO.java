package com.payment_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequestDTO {
    private Long userId;
    private Long Id;
    private Long roomId;
    private Double amount;
}
