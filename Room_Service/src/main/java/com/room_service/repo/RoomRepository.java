package com.room_service.repo;


import com.shared_persistence.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByAvailabilityStatus(String status);

    Optional<Room> findByRoomNumber(String roomNumber);
}


