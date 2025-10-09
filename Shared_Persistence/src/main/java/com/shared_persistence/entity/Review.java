package com.shared_persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Data
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "roomId", nullable = false)
    private Room room;

    @ManyToOne
    @JoinColumn(name = "id")
    private Booking booking;

    private int rating; // 1–5
    private String comment;
    private LocalDateTime createdAt = LocalDateTime.now();
}
