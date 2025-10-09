//package com.payment_service.contoller;
//import com.payment_service.dto.PaymentRequestDTO;
//import com.payment_service.dto.PaymentResponseDTO;
//import com.payment_service.service.PaymentService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/payments")
//public class PaymentController {
//
//    @Autowired
//    private PaymentService paymentService;
//
//    @PostMapping
//    public ResponseEntity<PaymentResponseDTO> makePayment(@RequestBody PaymentRequestDTO request) {
//        PaymentResponseDTO response = paymentService.makePayment(request);
//        return ResponseEntity.ok(response);
    //}
//}
package com.payment_service.contoller;

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

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentModelAssembler paymentAssembler;

    @PostMapping
    public ResponseEntity<EntityModel<PaymentResponseDTO>> makePayment(@RequestBody PaymentRequestDTO request) {
        PaymentResponseDTO response = paymentService.makePayment(request);
        return ResponseEntity.ok(paymentAssembler.toModel(response));
    }

//    @GetMapping
//    public ResponseEntity<CollectionModel<EntityModel<PaymentResponseDTO>>> getAllPayments() {
//        List<PaymentResponseDTO> payments = paymentService.getAllPayments();
//        return ResponseEntity.ok(paymentAssembler.toCollectionModel(payments));
//    }
}
