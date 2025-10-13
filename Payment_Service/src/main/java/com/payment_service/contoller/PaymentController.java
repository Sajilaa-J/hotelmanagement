
package com.payment_service.contoller;
//
//import com.payment_service.assembler.PaymentModelAssembler;
//import com.payment_service.dto.PaymentRequestDTO;
//import com.payment_service.dto.PaymentResponseDTO;
//import com.payment_service.service.PaymentService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.hateoas.EntityModel;
//import org.springframework.hateoas.CollectionModel;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/payments")
//@RequiredArgsConstructor
//public class PaymentController {
//
//    private final PaymentService paymentService;
//    private final PaymentModelAssembler paymentAssembler;
//
//    @PostMapping
//    public ResponseEntity<EntityModel<PaymentResponseDTO>> makePayment(@RequestBody PaymentRequestDTO request) {
//        PaymentResponseDTO response = paymentService.makePayment(request);
//        return ResponseEntity.ok(paymentAssembler.toModel(response));
//    }
//
////    @GetMapping
////    public ResponseEntity<CollectionModel<EntityModel<PaymentResponseDTO>>> getAllPayments() {
////        List<PaymentResponseDTO> payments = paymentService.getAllPayments();
////        return ResponseEntity.ok(paymentAssembler.toCollectionModel(payments));
////    }
//}

import com.payment_service.assembler.PaymentModelAssembler;
import com.payment_service.dto.PaymentRequestDTO;
import com.payment_service.dto.PaymentResponseDTO;
import com.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentModelAssembler paymentAssembler;

    @PostMapping
    public ResponseEntity<?> makePayment(@RequestBody PaymentRequestDTO request) {
        try {
            PaymentResponseDTO response = paymentService.makePayment(request);
            return ResponseEntity.ok(paymentAssembler.toModel(response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing payment: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPaymentsByUser(@PathVariable Long userId) {
        try {
            List<PaymentResponseDTO> payments = paymentService.getPaymentsByUser(userId);
            if (payments.isEmpty()) {
                return ResponseEntity.ok("No payments found for user with ID " + userId);
            }
            return ResponseEntity.ok(paymentAssembler.toCollectionModel(payments));
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving payments: " + e.getMessage());
        }
    }

    @GetMapping("/admin/all")
    public ResponseEntity<?> getAllPayments() {
        try {
            List<PaymentResponseDTO> payments = paymentService.getAllPayments();
            if (payments.isEmpty()) {
                return ResponseEntity.ok("No payments found");
            }
            return ResponseEntity.ok(paymentAssembler.toCollectionModel(payments));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving all payments: " + e.getMessage());
        }
    }


    @DeleteMapping("/user/{userId}/{paymentId}")
    public ResponseEntity<?> deletePayment(
            @PathVariable Long userId,
            @PathVariable Long paymentId
    ) {
        try {
            paymentService.deletePayment(userId, paymentId);
            return ResponseEntity.ok("Payment with ID " + paymentId + " deleted successfully for user " + userId);
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting payment: " + e.getMessage());
        }
    }



}
