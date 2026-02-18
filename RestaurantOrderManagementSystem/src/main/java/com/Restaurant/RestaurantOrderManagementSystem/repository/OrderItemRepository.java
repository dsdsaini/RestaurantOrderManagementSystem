package com.Restaurant.RestaurantOrderManagementSystem.repository;

import com.Restaurant.RestaurantOrderManagementSystem.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {}
