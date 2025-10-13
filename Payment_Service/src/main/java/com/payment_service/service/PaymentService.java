

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
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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
    private EmailService emailService;

    public PaymentResponseDTO makePayment(PaymentRequestDTO request) {


        if (request.getId() == null) {
            throw new IllegalArgumentException("Booking ID (id) must not be null");
        }
        if (request.getUserId() == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }
        if (request.getRoomId() == null) {
            throw new IllegalArgumentException("Room ID must not be null");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));
        Booking booking = bookingRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        Payment payment = new Payment();
        payment.setUser(user);
        payment.setRoom(room);
        payment.setBooking(booking);
        payment.setAmount(request.getAmount());
        payment.setPaymentStatus("SUCCESS");
        payment.setPaymentDate(LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);

        emailService.sendPaymentSuccessMail(
                user.getEmail(),
                user.getName(),
                booking.getId(),
                savedPayment.getAmount()
        );

        return PaymentResponseDTO.builder()
                //.paymentId(savedPayment.getPaymentId())
                .paymentStatus(savedPayment.getPaymentStatus())
                .amount(savedPayment.getAmount())
                .paymentDate(savedPayment.getPaymentDate())
                .build();
    }

    public List<PaymentResponseDTO> getPaymentsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        return paymentRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<PaymentResponseDTO> getAllPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private PaymentResponseDTO mapToResponse(Payment payment) {
        PaymentResponseDTO dto = new PaymentResponseDTO();
        dto.setId(payment.getPaymentId());
        dto.setUserId(payment.getUser().getUserId());
        dto.setBookingId(payment.getBooking().getId());
        dto.setAmount(payment.getAmount());
        dto.setPaymentStatus(payment.getPaymentStatus());
        dto.setPaymentDate(payment.getPaymentDate());
        return dto;
    }

    public void deletePayment(Long userId, Long paymentId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID " + userId));

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NoSuchElementException("Payment not found with ID " + paymentId));

        if (!payment.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("Payment does not belong to this user");
        }

        paymentRepository.delete(payment);
    }



}

