package com.shared_persistence.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @OneToOne
    @JoinColumn(name = "id",nullable = false)
    private Booking booking;



    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;


    @ManyToOne
    @JoinColumn(name = "roomId", nullable = false)
    private Room room;

    private LocalDateTime paymentDate = LocalDateTime.now();
    private double amount;
    private String paymentStatus;

}

