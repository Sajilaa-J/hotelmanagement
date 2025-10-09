package com.shared_persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String status;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Double totalAmount;

    // Room relationship
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    // User relationship
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
