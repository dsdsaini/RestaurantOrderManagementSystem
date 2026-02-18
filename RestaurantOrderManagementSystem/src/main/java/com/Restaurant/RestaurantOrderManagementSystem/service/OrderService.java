package com.Restaurant.RestaurantOrderManagementSystem.service;

import com.Restaurant.RestaurantOrderManagementSystem.entities.Order;
import com.Restaurant.RestaurantOrderManagementSystem.enums.OrderStatus;

import java.util.Map;

public interface OrderService {

    Order createOrder(Long branchId, String customerName, Map<Long, Integer> items, Map<Long, String> instructions, double deliveryCharge);
    Order updateStatus(Long orderId, String status);

    Order saveOrder(Order order);

    void updateOrderStatus(Long id, OrderStatus orderStatus);
}