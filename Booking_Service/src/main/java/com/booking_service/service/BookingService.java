package com.booking_service.service;

import com.booking_service.dto.BookingRequestDTO;
import com.booking_service.dto.BookingResponseDTO;
import com.shared_persistence.entity.Booking;
import com.shared_persistence.entity.Room;
import com.shared_persistence.entity.User;
import com.shared_persistence.repo.BookingRepository;
import com.shared_persistence.repo.RoomRepository;
import com.shared_persistence.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    public BookingService(BookingRepository bookingRepository, UserRepository userRepository, RoomRepository roomRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
    }

    // Create a booking
    public BookingResponseDTO createBooking(BookingRequestDTO req) {
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Room room = roomRepository.findById(req.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));


        Booking booking = new Booking();
        booking.setCheckInDate(req.getCheckInDate());
        booking.setCheckOutDate(req.getCheckOutDate());
        booking.setTotalAmount(req.getTotalAmount());
        booking.setStatus("PENDING");

        booking.setRoom(room);      // ← mandatory
        booking.setUser(user);

        Booking saved = bookingRepository.save(booking);
        return mapToResponse(saved);
    }

    // Get bookings for a specific user
    public List<BookingResponseDTO> getBookingsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return bookingRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Get all bookings (Admin)
    public List<BookingResponseDTO> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Update booking status (Admin)
    public BookingResponseDTO updateBookingStatus(Long bookingId, String status) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(status);
        Booking updated = bookingRepository.save(booking);
        return mapToResponse(updated);
    }

    private BookingResponseDTO mapToResponse(Booking booking) {
        BookingResponseDTO response = new BookingResponseDTO();
        response.setId(booking.getId());
        response.setRoomId(booking.getRoom().getRoomId());
        response.setUserId(booking.getUser().getUserId());
        response.setCheckInDate(booking.getCheckInDate());
        response.setCheckOutDate(booking.getCheckOutDate());
        response.setTotalAmount(booking.getTotalAmount());
        response.setStatus(booking.getStatus());
        return response;
    }
}

//import com.booking_service.dto.BookingRequestDTO;
//import com.booking_service.dto.BookingResponseDTO;
//import com.booking_service.repo.BookingRepository;
//import com.shared_persistence.entity.Booking;
//import com.shared_persistence.entity.User;
//import com.shared_persistence.repo.UserRepository;
//import org.springframework.stereotype.Service;
//
//@Service
//public class BookingService {
//
//    private final BookingRepository bookingRepository;
//    private final UserRepository userRepository;
//
//    public BookingService(BookingRepository bookingRepository, UserRepository userRepository) {
//        this.bookingRepository = bookingRepository;
//        this.userRepository = userRepository;
//    }
//
//    public BookingResponseDTO createBooking(BookingRequestDTO dto) {
//        // Fetch the user from DB
//        User user = userRepository.findById(dto.getUserId())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        // Map DTO to entity
//        Booking booking = new Booking();
//        booking.setCheckInDate(dto.getCheckInDate());
//        booking.setCheckOutDate(dto.getCheckOutDate());
//        booking.setRoomId(dto.getRoomId());
//        booking.setStatus("PENDING");
//        booking.setTotalAmount(dto.getTotalAmount());
//        booking.setUser(user);
//
//        // Save to DB
//        bookingRepository.save(booking);
//
//        // Prepare response DTO
//        BookingResponseDTO response = new BookingResponseDTO();
//        response.setBookingId(booking.getId());
//        response.setStatus(booking.getStatus());
//        response.setCheckInDate(booking.getCheckInDate());
//        response.setCheckOutDate(booking.getCheckOutDate());
//        response.setTotalAmount(booking.getTotalAmount());
//
//        return response;
//    }
//}
