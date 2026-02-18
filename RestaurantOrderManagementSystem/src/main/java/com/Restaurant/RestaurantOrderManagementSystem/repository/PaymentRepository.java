package com.Restaurant.RestaurantOrderManagementSystem.repository;

import com.Restaurant.RestaurantOrderManagementSystem.entities.Payment;
import com.Restaurant.RestaurantOrderManagementSystem.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing Payment entities.
 * <p>
 * Provides standard CRUD operations via JpaRepository and
 * custom methods to check payment existence based on order and status.
 * </p>
 */
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    /**
     * Checks if a payment exists for a given order with the specified status.
     * <p>
     * Useful to prevent duplicate payments or verify if an order has been fully paid.
     * </p>
     *
     * @param orderId the ID of the order
     * @param status  the payment status to check (PaymentStatus enum)
     * @return true if a payment with the given status exists for the order, false otherwise
     */
    boolean existsByOrderIdAndStatus(Long orderId, PaymentStatus status);
}
