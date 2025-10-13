package com.shared_persistence.repo;

import com.shared_persistence.entity.Payment;
import com.shared_persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByUser(User user);
}
