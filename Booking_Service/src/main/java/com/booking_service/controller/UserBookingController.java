
package com.booking_service.controller;
//
//import com.booking_service.assembler.BookingModelAssembler;
//import com.booking_service.dto.BookingRequestDTO;
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
//@RequestMapping("/api/user/bookings")
//@RequiredArgsConstructor
//public class UserBookingController {
//
//    private final BookingService bookingService;
//    private final BookingModelAssembler bookingAssembler;
//
//    @PostMapping
//    public ResponseEntity<EntityModel<BookingResponseDTO>> create(@RequestBody BookingRequestDTO req) {
//        BookingResponseDTO resp = bookingService.createBooking(req);
//        return ResponseEntity.ok(bookingAssembler.toModel(resp));
//    }
//
//    @GetMapping("/{userId}")
//    public ResponseEntity<CollectionModel<EntityModel<BookingResponseDTO>>> getUserBookings(@PathVariable Long userId) {
//        List<BookingResponseDTO> bookings = bookingService.getBookingsByUser(userId);
//        return ResponseEntity.ok(
//                bookingAssembler.toCollectionModel(bookings, UserBookingController.class, "getUserBookings")
//        );
//    }
//}

import com.booking_service.assembler.BookingModelAssembler;
import com.booking_service.dto.BookingRequestDTO;
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
@RequestMapping("/api/user/bookings")
@RequiredArgsConstructor
public class UserBookingController {

    private final BookingService bookingService;
    private final BookingModelAssembler bookingAssembler;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody BookingRequestDTO req) {
        try {
            BookingResponseDTO resp = bookingService.createBooking(req);
            return ResponseEntity.ok(bookingAssembler.toModel(resp));
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest()
                    .body("Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Error creating booking: " + e.getMessage());
        }
    }
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserBookings(@PathVariable Long userId) {
        try {
            List<BookingResponseDTO> bookings = bookingService.getBookingsByUser(userId);
            if (bookings.isEmpty()) {
                return ResponseEntity.ok("No bookings found for user with ID " + userId);
            }
            return ResponseEntity.ok(
                    bookingAssembler.toCollectionModel(bookings, UserBookingController.class, "getUserBookings")
            );
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest()
                    .body("Error: User with ID " + userId + " not found");
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Error retrieving bookings: " + e.getMessage());
        }
    }


    @PutMapping("/{bookingId}")
    public ResponseEntity<?> updateBooking(@PathVariable Long bookingId, @RequestBody BookingRequestDTO request) {
        try {
            BookingResponseDTO updated = bookingService.updateBooking(bookingId, request);
            return ResponseEntity.ok(bookingAssembler.toModel(updated));
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body("Error: Booking not found with ID " + bookingId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid update data: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating booking: " + e.getMessage());
        }
    }


    @DeleteMapping("/{bookingId}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable Long bookingId) {
        try {
            bookingService.cancelBooking(bookingId);
            return ResponseEntity.ok("Booking with ID " + bookingId + " has been cancelled successfully.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body("Error: Booking not found with ID " + bookingId);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error cancelling booking: " + e.getMessage());
        }
    }
}


