package com.booking_service.controller;

//import com.booking_service.assembler.BookingModelAssembler;
//import com.booking_service.dto.BookingResponseDTO;
//import com.booking_service.service.BookingService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.hateoas.CollectionModel;
//import org.springframework.hateoas.EntityModel;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/admin/bookings")
//@RequiredArgsConstructor
//public class AdminBookingController {
//
//    private final BookingService bookingService;
//    private final BookingModelAssembler bookingAssembler;
//
//    @GetMapping
//    public ResponseEntity<CollectionModel<EntityModel<BookingResponseDTO>>> getAllBookings() {
//        List<BookingResponseDTO> bookings = bookingService.getAllBookings();
//        return ResponseEntity.ok(bookingAssembler.toCollectionModel(bookings, AdminBookingController.class, "getAllBookings"));
//    }
//
//    @PutMapping("/{bookingId}/status")
//    public ResponseEntity<EntityModel<BookingResponseDTO>> updateBookingStatus(
//            @PathVariable Long bookingId,
//            @RequestParam String status
//    ) {
//        BookingResponseDTO updated = bookingService.updateBookingStatus(bookingId, status);
//        return ResponseEntity.ok(bookingAssembler.toModel(updated));
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
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/admin/bookings")
@RequiredArgsConstructor
public class AdminBookingController {

    private final BookingService bookingService;
    private final BookingModelAssembler bookingAssembler;

    @GetMapping
    public ResponseEntity<?> getAllBookings() {
        List<BookingResponseDTO> bookings = bookingService.getAllBookings();
        if (bookings.isEmpty()) {
            return ResponseEntity.ok("No bookings found");
        }
        return ResponseEntity.ok(bookingAssembler.toCollectionModel(bookings, AdminBookingController.class, "getAllBookings"));
    }

    @PutMapping("/{bookingId}/status")
    public ResponseEntity<?> updateBookingStatus(
            @PathVariable Long bookingId,
            @RequestParam String status
    ) {
        try {
            BookingResponseDTO updated = bookingService.updateBookingStatus(bookingId, status);
            return ResponseEntity.ok(bookingAssembler.toModel(updated));
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest()
                    .body("Error: Booking with ID " + bookingId + " not found.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Error updating booking: " + e.getMessage());
        }
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<?> deleteBooking(@PathVariable Long bookingId) {
        try {
            bookingService.deleteBooking(bookingId);
            return ResponseEntity.ok("Booking with ID " + bookingId + " has been deleted successfully.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest()
                    .body("Error: Booking with ID " + bookingId + " not found.");
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Error deleting booking: " + e.getMessage());
        }
    }

}

