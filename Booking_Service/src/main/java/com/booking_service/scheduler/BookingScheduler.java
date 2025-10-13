//package com.booking_service.scheduler;
//import com.shared_persistence.entity.Booking;
//import com.shared_persistence.entity.Room;
//import com.shared_persistence.repo.BookingRepository;
//import com.shared_persistence.repo.RoomRepository;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@Service
//public class BookingScheduler {
//
//    private final BookingRepository bookingRepository;
//    private final RoomRepository roomRepository;
//
//    public BookingScheduler(BookingRepository bookingRepository, RoomRepository roomRepository) {
//        this.bookingRepository = bookingRepository;
//        this.roomRepository = roomRepository;
//    }
//
//    // Runs every day at midnight
//    @Scheduled(cron = "0 0 0 * * ?")
//    @Transactional
//    public void updateRoomStatus() {
//        LocalDate today = LocalDate.now();
//        List<Booking> expiredBookings = bookingRepository.findByEndDateBeforeAndStatus(today, BookingStatus.BOOKED);
//
//        for (Booking booking : expiredBookings) {
//            booking.setStatus(BookingStatus.AVAILABLE);
//            bookingRepository.save(booking);
//
//            Room room = roomRepository.findById(booking.getId()).orElse(null);
//            if (room != null) {
//                room.setStatus(RoomStatus.AVAILABLE);
//                roomRepository.save(room);
//            }
//        }
//    }
//}