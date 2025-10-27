package com.booking_service.scheduler;

import com.booking_service.kafka.BookingProducer;
import com.shared_persistence.entity.Booking;
import com.shared_persistence.entity.Room;
import com.shared_persistence.repo.BookingRepository;
import com.shared_persistence.repo.RoomRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RoomAvailabilityScheduler {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final BookingProducer bookingProducer;

    public RoomAvailabilityScheduler(BookingRepository bookingRepository, RoomRepository roomRepository, BookingProducer bookingProducer) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.bookingProducer = bookingProducer;
    }
    //@Scheduled(cron = "*/5 * * * * *") // every 5 sec
//    @Scheduled(cron = "0 0 * * * *")
//    @Transactional
//    public void updateExpiredBookings() {
//        LocalDate today = LocalDate.now();
//        System.out.println("Scheduler running at: " + today);
//
//
//        List<Booking> expiredBookings = bookingRepository.findByCheckOutDateBefore(today);
//        System.out.println("Expired bookings found: " + expiredBookings.size());
//
//        for (Booking booking : expiredBookings) {
//            Room room = booking.getRoom();
//            if (room != null) {
//                System.out.println("Room: " + room.getRoomNumber() + " current status: " + room.getAvailabilityStatus());
//
//
//                if ("Booked".equalsIgnoreCase(room.getAvailabilityStatus())) {
//                    room.setAvailabilityStatus("Available");
//                    roomRepository.save(room); // must save
//                    System.out.println("Room " + room.getRoomNumber() + " set to Available");
//                }
//
//
//                booking.setStatus("COMPLETED");
//                bookingRepository.save(booking);
//            }
//        }
//    }

    //@Scheduled(cron = "0 0 * * * *")
    @Scheduled(cron = "*/5 * * * * *")
    @Transactional
    public void releaseExpiredBookings() {
        LocalDate today = LocalDate.now();
        List<Booking> expiredBookings = bookingRepository.findByCheckOutDateBefore(today);

        System.out.println("Scheduler running at: " + today + ", expired bookings: " + expiredBookings.size());

        for (Booking booking : expiredBookings) {
            bookingProducer.sendRoomReleasedEvent(booking.getRoom().getRoomId());





            booking.setStatus("COMPLETED");
            bookingRepository.save(booking);

            System.out.println("✅ Released room ID: " + booking.getRoom().getRoomId());
        }
    }

}

