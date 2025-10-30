package com.shared_persistence.repo;


import com.shared_persistence.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room> {
    List<Room> findByAvailabilityStatus(String status);

    Optional<Room> findById(Long roomId);
}
