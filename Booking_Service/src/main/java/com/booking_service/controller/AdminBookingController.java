package com.booking_service.controller;
//
//import com.booking_service.dto.BookingResponseDTO;
//import com.booking_service.service.BookingService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/admin/bookings")
//public class AdminBookingController {
//
//    private final BookingService bookingService;
//
//    public AdminBookingController(BookingService bookingService) {
//        this.bookingService = bookingService;
//    }
//
//    @GetMapping
//    public ResponseEntity<List<BookingResponseDTO>> getAllBookings() {
//        return ResponseEntity.ok(bookingService.getAllBookings());
//    }
//
//    @PutMapping("/{bookingId}/status")
//    public ResponseEntity<BookingResponseDTO> updateBookingStatus(
//            @PathVariable Long bookingId,
//            @RequestParam String status
//    ) {
//        return ResponseEntity.ok(bookingService.updateBookingStatus(bookingId, status));
//    }
//}

import com.booking_service.assembler.BookingModelAssembler;
import com.booking_service.dto.BookingResponseDTO;
import com.booking_service.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/bookings")
@RequiredArgsConstructor
public class AdminBookingController {

    private final BookingService bookingService;
    private final BookingModelAssembler bookingAssembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<BookingResponseDTO>>> getAllBookings() {
        List<BookingResponseDTO> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookingAssembler.toCollectionModel(bookings, AdminBookingController.class, "getAllBookings"));
    }

    @PutMapping("/{bookingId}/status")
    public ResponseEntity<EntityModel<BookingResponseDTO>> updateBookingStatus(
            @PathVariable Long bookingId,
            @RequestParam String status
    ) {
        BookingResponseDTO updated = bookingService.updateBookingStatus(bookingId, status);
        return ResponseEntity.ok(bookingAssembler.toModel(updated));
    }
}



