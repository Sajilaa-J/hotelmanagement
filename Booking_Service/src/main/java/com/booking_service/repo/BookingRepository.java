//package com.booking_service.repo;
//
//import com.shared_persistence.entity.Booking;
//import com.shared_persistence.entity.User;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//
//public interface BookingRepository extends JpaRepository<Booking, Long> {
//    List<Booking> findByUser(User user);  // Use User entity instead of userId
//}
//
