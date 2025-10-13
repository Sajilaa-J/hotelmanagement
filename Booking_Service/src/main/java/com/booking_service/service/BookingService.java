package com.booking_service.service;

import com.booking_service.client.RoomClient;
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
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final RoomClient roomClient;
    public BookingService(BookingRepository bookingRepository, UserRepository userRepository, RoomRepository roomRepository, RoomClient roomClient) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.roomClient=roomClient;
    }


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

        booking.setRoom(room);
        booking.setUser(user);

        //roomClient.updateRoomStatus(req.getRoomId(), "BOOKED");
//        roomClient.updateRoomStatus(req.getRoomId(), "BOOKED");
        roomClient.updateRoomStatus(req.getRoomId(), "BOOKED","SYSTEM");




        Booking saved = bookingRepository.save(booking);
        return mapToResponse(saved);
    }

    public List<BookingResponseDTO> getBookingsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return bookingRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    public List<BookingResponseDTO> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

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
    public BookingResponseDTO updateBooking(Long bookingId, BookingRequestDTO request) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NoSuchElementException("Booking not found with ID " + bookingId));

        booking.setCheckInDate(request.getCheckInDate());
        booking.setCheckOutDate(request.getCheckOutDate());
        booking.setTotalAmount(request.getTotalAmount());
        if (request.getRoomId() != null) {
            Room room = roomRepository.findById(request.getRoomId())
                    .orElseThrow(() -> new RuntimeException("Room not found"));
            booking.setRoom(room);
        }

        Booking updated = bookingRepository.save(booking);
        return mapToResponse(updated);
    }

    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NoSuchElementException("Booking not found"));

        bookingRepository.delete(booking);
    }

    public void deleteBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NoSuchElementException("Booking not found with ID " + bookingId));
        bookingRepository.delete(booking);
    }


}

