//package com.booking_service.controller;
//
//
//
//import com.booking_service.dto.BookingRequestDTO;
//import com.booking_service.dto.BookingResponseDTO;
//import com.booking_service.service.BookingService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/user/bookings")
//public class UserBookingController {
//
//    private final BookingService bookingService;
//
//    public UserBookingController(BookingService bookingService) {
//        this.bookingService = bookingService;
//    }
//
//
//    @PostMapping
//    public ResponseEntity<BookingResponseDTO> create(@RequestBody BookingRequestDTO req) {
//        BookingResponseDTO resp = bookingService.createBooking(req);
//        return ResponseEntity.ok(resp);
//    }
//
//
//    @GetMapping("/{userId}")
//    public ResponseEntity<List<BookingResponseDTO>> getUserBookings(@PathVariable Long userId) {
//        return ResponseEntity.ok(bookingService.getBookingsByUser(userId));
//    }
//}
package com.booking_service.controller;

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

@RestController
@RequestMapping("/api/user/bookings")
@RequiredArgsConstructor
public class UserBookingController {

    private final BookingService bookingService;
    private final BookingModelAssembler bookingAssembler;

    @PostMapping
    public ResponseEntity<EntityModel<BookingResponseDTO>> create(@RequestBody BookingRequestDTO req) {
        BookingResponseDTO resp = bookingService.createBooking(req);
        return ResponseEntity.ok(bookingAssembler.toModel(resp));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CollectionModel<EntityModel<BookingResponseDTO>>> getUserBookings(@PathVariable Long userId) {
        List<BookingResponseDTO> bookings = bookingService.getBookingsByUser(userId);
        return ResponseEntity.ok(
                bookingAssembler.toCollectionModel(bookings, UserBookingController.class, "getUserBookings")
        );
    }
}


