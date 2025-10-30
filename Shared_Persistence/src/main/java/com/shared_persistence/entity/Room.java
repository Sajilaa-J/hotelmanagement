package com.shared_persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    //@Column(name = "room_number", unique = true, nullable = false)
    private String roomNumber;
    @NotBlank(message = "Room type is required")
    @Pattern(
            regexp = "^(Single|Double|Suite|Deluxe)$",
            message = "Invalid room type. Allowed types: Single, Double, Suite, Deluxe"
    )
    private String type;
    private double price;


    @NotBlank(message = "Availability status is required")
    @Pattern(
            regexp = "^(AVAILABLE|BOOKED)$",
            message = "Invalid status. Only 'Available' or 'Booked' are allowed"
    )
    private String availabilityStatus;
    private String description;
    private String updatedBy;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}
//package com.shared_persistence.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//import java.util.List;
//
//@Entity
//@Table(name = "rooms")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class Room {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long roomId;
//
//    private String roomNumber;
//
//    private String type; // Single, Double, Suite
//
//    private double price;
//
//    @Enumerated(EnumType.STRING)
//    private AvailabilityStatus availabilityStatus; // Available / Booked
//
//    private String description;
//
//    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Booking> bookings;
//
//    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Review> reviews;
//
//    public enum AvailabilityStatus {
//        AVAILABLE,
//        BOOKED
//    }
//}


//import jakarta.persistence.*;
//        import lombok.*;
//
//@Entity
//@Table(name = "rooms")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class Room {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long roomId;
//
//    @Column(nullable = false, unique = true)
//    private String roomNumber;
//
//    @Column(nullable = false)
//    private String type; // Single, Double, Suite
//
//    @Column(nullable = false)
//    private double price;
//
//    @Column(nullable = false)
//    private String availabilityStatus; // Available / Booked
//
//    private String description;
//    @ManyToOne
//    @JoinColumn(name = "userId")
//    private User user;
//}

