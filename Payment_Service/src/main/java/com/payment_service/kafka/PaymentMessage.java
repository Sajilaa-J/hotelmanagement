//package com.payment_service.kafka;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.io.Serializable;
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class PaymentMessage implements Serializable {
//
//    private UUID eventId;
//    private Long paymentId;
//    private Long bookingId;
//    private Long userId;
//    private Long roomId;
//    private LocalDateTime paymentDate;
//    private double amount;
//    private String paymentStatus;
//
//
//
//    @Override
//    public String toString() {
//        return "PaymentMessage{" +
//                "eventId=" + eventId +
//                ", paymentId=" + paymentId +
//                ", bookingId=" + bookingId +
//                ", userId=" + userId +
//                ", roomId=" + roomId +
//                ", paymentDate=" + paymentDate +
//                ", amount=" + amount +
//                ", paymentStatus='" + paymentStatus + '\'' +
//                '}';
//    }
//}
