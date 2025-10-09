//
//package com.payment_service.service;
//
//import com.payment_service.dto.PaymentRequestDTO;
//import com.payment_service.dto.PaymentResponseDTO;
//import com.shared_persistence.entity.Payment;
//import com.shared_persistence.entity.Room;
//import com.shared_persistence.entity.User;
//import com.shared_persistence.entity.Booking;
//import com.shared_persistence.repo.PaymentRepository;
//import com.shared_persistence.repo.RoomRepository;
//import com.shared_persistence.repo.UserRepository;
//import com.shared_persistence.repo.BookingRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//
//@Service
//public class PaymentService {
//
//    @Autowired
//    private PaymentRepository paymentRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private RoomRepository roomRepository;
//
//    @Autowired
//    private BookingRepository bookingRepository;
//
//    public PaymentResponseDTO makePayment(PaymentRequestDTO request) {
//
//        // Validate input fields
//        if (request.getId() == null) {
//            throw new IllegalArgumentException("Booking ID (id) must not be null");
//        }
//        if (request.getUserId() == null) {
//            throw new IllegalArgumentException("User ID must not be null");
//        }
//        if (request.getRoomId() == null) {
//            throw new IllegalArgumentException("Room ID must not be null");
//        }
//
//        // Fetch User, Room, and Booking entities
//        User user = userRepository.findById(request.getUserId())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Room room = roomRepository.findById(request.getRoomId())
//                .orElseThrow(() -> new RuntimeException("Room not found"));
//
//        Booking booking = bookingRepository.findById(request.getId())
//                .orElseThrow(() -> new RuntimeException("Booking not found"));
//
//        // Create and save Payment entity
//        Payment payment = new Payment();
//        payment.setUser(user);
//        payment.setRoom(room);
//        payment.setBooking(booking);
//        payment.setAmount(request.getAmount());
//        payment.setPaymentStatus("SUCCESS");
//        payment.setPaymentDate(LocalDateTime.now());
//        Payment savedPayment = paymentRepository.save(payment);
//
//        // Return simplified response
//        PaymentResponseDTO response = new PaymentResponseDTO();
//        response.setPaymentId(savedPayment.getPaymentId());
//        response.setPaymentStatus("SUCCESS");
//        response.setPaymentDate(savedPayment.getPaymentDate());
//        response.setAmount(savedPayment.getAmount());
//        return response;
//
//    }
//}

package com.payment_service.service;

import com.payment_service.dto.PaymentRequestDTO;
import com.payment_service.dto.PaymentResponseDTO;
import com.shared_persistence.entity.Payment;
import com.shared_persistence.entity.Room;
import com.shared_persistence.entity.User;
import com.shared_persistence.entity.Booking;
import com.shared_persistence.repo.PaymentRepository;
import com.shared_persistence.repo.RoomRepository;
import com.shared_persistence.repo.UserRepository;
import com.shared_persistence.repo.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EmailService emailService; // ✅ Add this

    public PaymentResponseDTO makePayment(PaymentRequestDTO request) {

        // Validate input fields
        if (request.getId() == null) {
            throw new IllegalArgumentException("Booking ID (id) must not be null");
        }
        if (request.getUserId() == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }
        if (request.getRoomId() == null) {
            throw new IllegalArgumentException("Room ID must not be null");
        }

        // Fetch entities
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));
        Booking booking = bookingRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Create Payment
        Payment payment = new Payment();
        payment.setUser(user);
        payment.setRoom(room);
        payment.setBooking(booking);
        payment.setAmount(request.getAmount());
        payment.setPaymentStatus("SUCCESS");
        payment.setPaymentDate(LocalDateTime.now());

        // Save payment
        Payment savedPayment = paymentRepository.save(payment);

        // ✅ Send email after successful payment
        emailService.sendPaymentSuccessMail(
                user.getEmail(),
                user.getName(),
                booking.getId(),
                savedPayment.getAmount()
        );

        // Build and return response
        return PaymentResponseDTO.builder()
                //.paymentId(savedPayment.getPaymentId())
                .paymentStatus(savedPayment.getPaymentStatus())
                .amount(savedPayment.getAmount())
                .paymentDate(savedPayment.getPaymentDate())
                .build();
    }
}

