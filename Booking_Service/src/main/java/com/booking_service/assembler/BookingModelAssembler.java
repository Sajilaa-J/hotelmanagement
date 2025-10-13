package com.booking_service.assembler;
import com.booking_service.controller.AdminBookingController;
import com.booking_service.controller.UserBookingController;
import com.booking_service.dto.BookingResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class BookingModelAssembler implements RepresentationModelAssembler<BookingResponseDTO, EntityModel<BookingResponseDTO>> {

    @Override
    public EntityModel<BookingResponseDTO> toModel(BookingResponseDTO booking) {
        EntityModel<BookingResponseDTO> model = EntityModel.of(booking);


        model.add(linkTo(methodOn(AdminBookingController.class)
                .updateBookingStatus(booking.getId(), booking.getStatus())).withRel("update-status"));
        model.add(linkTo(methodOn(AdminBookingController.class)
                .getAllBookings()).withRel("all-bookings"));
        model.add(linkTo(methodOn(AdminBookingController.class)
                .deleteBooking(booking.getId())).withRel("delete-booking"));

        model.add(linkTo(methodOn(UserBookingController.class)
                .getUserBookings(booking.getUserId())).withRel("user-bookings"));
        model.add(linkTo(methodOn(UserBookingController.class)
                .updateBooking(booking.getId(), null)).withRel("update-booking"));
        model.add(linkTo(methodOn(UserBookingController.class)
                .cancelBooking(booking.getId())).withRel("cancel-booking"));


        return model;
    }

    public CollectionModel<EntityModel<BookingResponseDTO>> toCollectionModel(List<BookingResponseDTO> bookings, Class<?> controllerClass, String selfMethod) {
        List<EntityModel<BookingResponseDTO>> bookingModels = bookings.stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<BookingResponseDTO>> collection = CollectionModel.of(bookingModels);

        try {
            collection.add(linkTo(controllerClass.getMethod(selfMethod, Long.class)).withSelfRel());
        } catch (NoSuchMethodException e) {

            try {
                collection.add(linkTo(controllerClass.getMethod(selfMethod)).withSelfRel());
            } catch (NoSuchMethodException ex) {
                ex.printStackTrace();
            }
        }

        return collection;
    }
}
