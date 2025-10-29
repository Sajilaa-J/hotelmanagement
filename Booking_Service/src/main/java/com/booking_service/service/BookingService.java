package com.booking_service.service;

import com.booking_service.client.RoomClient;
import com.booking_service.dto.BookingRequestDTO;
import com.booking_service.dto.BookingResponseDTO;
import com.booking_service.kafka.BookingProducer;
import com.shared_persistence.entity.Booking;
import com.shared_persistence.entity.Room;
import com.shared_persistence.entity.User;
import com.shared_persistence.repo.BookingRepository;
import com.shared_persistence.repo.RoomRepository;
import com.shared_persistence.repo.UserRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class BookingService {


    @Qualifier("applicationTaskExecutor")
    @Autowired
    private final TaskExecutor taskExecutor;

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final RoomClient roomClient;
    private final BookingProducer bookingProducer;
    //private final BookingRepositoryCustom bookingRepositoryCustom;
    @Autowired
    public BookingService(
            BookingRepository bookingRepository,
            UserRepository userRepository,
            RoomRepository roomRepository,
            RoomClient roomClient,
            @Qualifier("applicationTaskExecutor") TaskExecutor taskExecutor, BookingProducer bookingProducer
    ) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.roomClient = roomClient;
        this.taskExecutor = taskExecutor;
        this.bookingProducer = bookingProducer;
        //this.bookingRepositoryCustom = bookingRepositoryCustom;
    }

    public BookingResponseDTO createBooking(BookingRequestDTO req) {
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Room room = roomRepository.findById(req.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

//        if (!"Available".equalsIgnoreCase(room.getAvailabilityStatus())) {
//            throw new RuntimeException("Room not available");
//        }

        // Update room status to booked
           room.setAvailabilityStatus("Booked");
        roomRepository.save(room);
//        if ("BOOKED".equalsIgnoreCase(room.getAvailabilityStatus())) {
//            throw new RuntimeException("Room is not available for this date!");
//        }
//        boolean overlapExists =bookingRepositoryCustom.existsByRoomAndDateRange(
//                room,
//                req.getCheckInDate(),
//                req.getCheckOutDate()
//        );

        boolean overlapExists = bookingRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("room"), room));
            predicates.add(
                    cb.and(
                            cb.lessThanOrEqualTo(root.get("checkInDate"), req.getCheckOutDate()),
                            cb.greaterThanOrEqualTo(root.get("checkOutDate"), req.getCheckInDate())
                    )
            );
            return cb.and(predicates.toArray(new Predicate[0]));
        }).stream().findAny().isPresent();


        System.out.println("Overlap Exists? " + overlapExists);
//        if (overlapExists) {
//            throw new RuntimeException("Room is not available for this date!");
//        }

        if (overlapExists) {
            throw new RuntimeException("Room is not available for this date!");
        } else {
            System.out.println("Room is available!");
        }

        Booking booking = new Booking();
        booking.setCheckInDate(req.getCheckInDate());
        booking.setCheckOutDate(req.getCheckOutDate());
        booking.setTotalAmount(req.getTotalAmount());
        booking.setStatus("BOOKED");
        booking.setRoom(room);
        booking.setUser(user);

        Booking saved = bookingRepository.save(booking);

       // roomClient.updateRoomStatus(req.getRoomId(), "BOOKED", "SYSTEM");
        // Send Kafka message
//        String message = "RoomBooked:" + req.getRoomId();
//        bookingProducer.sendBookingEvent(message);

        bookingProducer.sendBookingEvent(req.getRoomId(), req.getUserId());

        return mapToResponse(saved);
    }



//    public BookingResponseDTO createBooking(BookingRequestDTO req) {
//        User user = userRepository.findById(req.getUserId())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Room room = roomRepository.findById(req.getRoomId())
//                .orElseThrow(() -> new RuntimeException("Room not found"));
//
//
//        Booking booking = new Booking();
//        booking.setCheckInDate(req.getCheckInDate());
//        booking.setCheckOutDate(req.getCheckOutDate());
//        booking.setTotalAmount(req.getTotalAmount());
//        booking.setStatus("PENDING");
//
//        booking.setRoom(room);
//        booking.setUser(user);
//
//        //roomClient.updateRoomStatus(req.getRoomId(), "BOOKED");
////        roomClient.updateRoomStatus(req.getRoomId(), "BOOKED");
//        roomClient.updateRoomStatus(req.getRoomId(), "BOOKED","SYSTEM");
//
//
//
//
//        Booking saved = bookingRepository.save(booking);
//        return mapToResponse(saved);
//    }

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

