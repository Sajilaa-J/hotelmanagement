package com.shared_persistence.repo;

import com.shared_persistence.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByUserUserId(Long userId); // fetch reviews by user ID
    List<Review> findByRoomRoomId(Long roomId); // fetch reviews by room ID
}
