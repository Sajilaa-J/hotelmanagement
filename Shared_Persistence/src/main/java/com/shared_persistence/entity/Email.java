package com.shared_persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "email_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emailId;

    // ✅ Reference IDs from other services — not entity relationships
    @Column(nullable = false)
    private Long userId;  // User who receives the email

    private Long bookingId; // Link to Booking (from Booking Service)
    private Long paymentId; // Link to Payment (from Payment Service)

    @Column(nullable = false)
    private String recipientEmail;

    @Column(nullable = false)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String body;

    private String emailType; // e.g. BOOKING_CONFIRMATION, PAYMENT_RECEIPT, CANCELLATION
    private String status;    // e.g. SENT, FAILED, PENDING
    private LocalDateTime sentAt = LocalDateTime.now();
}
