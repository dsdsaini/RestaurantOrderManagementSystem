package com.Restaurant.RestaurantOrderManagementSystem.repository;

import com.Restaurant.RestaurantOrderManagementSystem.entities.Payment;
import com.Restaurant.RestaurantOrderManagementSystem.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    boolean existsByOrderIdAndStatus(Long orderId, PaymentStatus status);
}