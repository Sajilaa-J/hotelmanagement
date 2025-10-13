package com.booking_service.scheduler;

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

    public RoomAvailabilityScheduler(BookingRepository bookingRepository, RoomRepository roomRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
    }

    // Runs every day at 00:00
    //@Scheduled(cron = "0 0 0 * * ?")
//    @Scheduled(cron = "0 0 0 * * ?")
//    @Scheduled(cron = "*/5 * * * * *")  // every 5 seconds
//    @Transactional
//    public void updateExpiredBookings() {
//        LocalDate today = LocalDate.now();
//
//        // Fetch all bookings that have ended
//        List<Booking> expiredBookings = bookingRepository.findByCheckOutDateBefore(today);
//
//
//        for (Booking booking : expiredBookings) {
//            Room room = booking.getRoom(); // get the room from booking
//            if (room != null && "Booked".equalsIgnoreCase(room.getAvailabilityStatus())) {
//                room.setAvailabilityStatus("Available");
//                roomRepository.save(room);
//            }
//        }
//    }
    @Scheduled(cron = "*/5 * * * * *") // every 5 sec
    @Transactional
    public void updateExpiredBookings() {
        LocalDate today = LocalDate.now();
        System.out.println("Scheduler running at: " + today);

        List<Booking> expiredBookings = bookingRepository.findByCheckOutDateBefore(today);
        System.out.println("Expired bookings found: " + expiredBookings.size());

        for (Booking booking : expiredBookings) {
            Room room = booking.getRoom();
            if (room != null) {
                System.out.println("Room: " + room.getRoomNumber() + " current status: " + room.getAvailabilityStatus());
                if ("Booked".equalsIgnoreCase(room.getAvailabilityStatus())) {
                    room.setAvailabilityStatus("Available");
                    roomRepository.save(room);
                    System.out.println("Room " + room.getRoomNumber() + " set to Available");
                }
            }
        }
    }


}

