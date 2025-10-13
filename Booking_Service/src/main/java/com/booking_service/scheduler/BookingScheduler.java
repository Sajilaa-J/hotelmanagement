package com.booking_service.scheduler;

import com.booking_service.client.RoomClient;
import com.shared_persistence.entity.Booking;
import com.shared_persistence.repo.BookingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class BookingScheduler {

    private final BookingRepository bookingRepository;
    private final RoomClient roomClient;

    @Value("${admin.token}")
    private String adminToken;

    public BookingScheduler(BookingRepository bookingRepository, RoomClient roomClient) {
        this.bookingRepository = bookingRepository;
        this.roomClient = roomClient;
    }


    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void releaseExpiredRooms() {
        LocalDate today = LocalDate.now();

        List<Booking> expiredBookings = bookingRepository.findAllByCheckOutDateBeforeAndStatus(today, "BOOKED");

        for (Booking booking : expiredBookings) {
            try {
                roomClient.updateRoomStatus(
                        booking.getRoom().getRoomId(),
                        "AVAILABLE",
                        adminToken
                );

                booking.setStatus("COMPLETED");
                bookingRepository.save(booking);

                System.out.println("Room " + booking.getRoom().getRoomId() + " released successfully.");

            } catch (Exception e) {
                System.err.println("Failed to update room " + booking.getRoom().getRoomId() + ": " + e.getMessage());
            }
        }
    }
}

