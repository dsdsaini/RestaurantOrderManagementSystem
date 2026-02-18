package com.Restaurant.RestaurantOrderManagementSystem.repository;

import com.Restaurant.RestaurantOrderManagementSystem.entities.Order;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repository interface for managing Order entities.
 * <p>
 * Provides standard CRUD operations via JpaRepository,
 * and custom methods for locking orders to prevent concurrent updates.
 * </p>
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Fetches an Order by its ID with a pessimistic write lock.
     * <p>
     * This method is typically used when updating an order to ensure
     * that no other transaction can modify it concurrently.
     * </p>
     *
     * @param id the ID of the order to fetch
     * @return an Optional containing the Order if found, otherwise empty
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT o FROM Order o WHERE o.id = :id")
    Optional<Order> findByIdForUpdate(@Param("id") Long id);
}
