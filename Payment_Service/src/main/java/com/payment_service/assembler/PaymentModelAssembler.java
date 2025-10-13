package com.payment_service.assembler;


import com.payment_service.contoller.PaymentController;
import com.payment_service.dto.PaymentResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PaymentModelAssembler implements RepresentationModelAssembler<PaymentResponseDTO, EntityModel<PaymentResponseDTO>> {

    @Override
    public EntityModel<PaymentResponseDTO> toModel(PaymentResponseDTO payment) {
        EntityModel<PaymentResponseDTO> model = EntityModel.of(payment);


        model.add(linkTo(methodOn(PaymentController.class)
                .makePayment(null)).withRel("make-payment"));
        model.add(linkTo(methodOn(PaymentController.class)
                .getPaymentsByUser(payment.getUserId())).withRel("user-payments"));
        model.add(linkTo(methodOn(PaymentController.class)
                .deletePayment(payment.getUserId(), payment.getId())).withRel("delete-payment"));


        model.add(linkTo(methodOn(PaymentController.class)
                .getAllPayments()).withRel("all-payments"));

        return model;
    }

    public CollectionModel<EntityModel<PaymentResponseDTO>> toCollectionModel(List<PaymentResponseDTO> payments) {
        List<EntityModel<PaymentResponseDTO>> paymentModels = payments.stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(paymentModels);
    }
}
