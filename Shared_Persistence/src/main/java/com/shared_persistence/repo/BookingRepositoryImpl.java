//package com.shared_persistence.repo;
//import com.shared_persistence.entity.Booking;
//import com.shared_persistence.entity.Room;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.persistence.criteria.CriteriaBuilder;
//import jakarta.persistence.criteria.CriteriaQuery;
//import jakarta.persistence.criteria.Predicate;
//import jakarta.persistence.criteria.Root;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDate;
//
//@Repository
//public class BookingRepositoryImpl implements BookingRepositoryCustom {
//
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    public boolean existsByRoomAndDateRange(Room room, LocalDate checkInDate, LocalDate checkOutDate) {
//
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Long> query = cb.createQuery(Long.class);
//        Root<Booking> booking = query.from(Booking.class);
//        Predicate roomMatch = cb.equal(booking.get("room"), room);
//        Predicate checkOutAfterCheckInDate = cb.greaterThanOrEqualTo(booking.get("checkOutDate"), checkInDate);
//        Predicate checkInBeforeCheckOutDate = cb.lessThanOrEqualTo(booking.get("checkInDate"), checkOutDate);
//        Predicate statusBooked = cb.equal(booking.get("status"), "BOOKED");
//        Predicate finalCondition = cb.and(roomMatch, checkOutAfterCheckInDate, checkInBeforeCheckOutDate, statusBooked);
//        query.select(cb.count(booking)).where(finalCondition);
//
//        Long count = entityManager.createQuery(query).getSingleResult();
//
//        return count > 0;
//    }
//}
