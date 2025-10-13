package com.shared_persistence.repo;

import com.shared_persistence.entity.Booking;
import com.shared_persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(User user);

    Optional<Booking> findById(Long id);
    List<Booking> findAllByCheckOutDateBeforeAndStatus(LocalDate date, String status);

}

