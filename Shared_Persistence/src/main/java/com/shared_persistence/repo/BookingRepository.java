package com.shared_persistence.repo;

import com.shared_persistence.entity.Booking;
import com.shared_persistence.entity.Room;
import com.shared_persistence.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> , JpaSpecificationExecutor<Booking> {

    List<Booking> findByUser(User user);

    Optional<Booking> findById(Long id);

    List<Booking> findByCheckOutDateBefore(LocalDate date);

//    boolean existsByRoomAndDateRange(Room room, LocalDate checkInDate, LocalDate checkOutDate);


//    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END " +
//            "FROM Booking b " +
//            "WHERE b.room = :room " +
//            "AND b.checkOutDate >= :checkInDate " +
//            "AND b.checkInDate <= :checkOutDate " +
//            "AND b.status = 'BOOKED'")
//    boolean existsByRoomAndDateRange(
//            @Param("room") Room room,
//            @Param("checkInDate") LocalDate checkInDate,
//            @Param("checkOutDate") LocalDate checkOutDate
//    );


}

