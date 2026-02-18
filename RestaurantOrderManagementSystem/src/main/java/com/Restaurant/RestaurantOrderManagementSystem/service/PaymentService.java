package com.Restaurant.RestaurantOrderManagementSystem.service;


import com.Restaurant.RestaurantOrderManagementSystem.entities.Payment;
import com.Restaurant.RestaurantOrderManagementSystem.enums.PaymentMethod;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface PaymentService {


    @Transactional
    Payment processPayment(Long orderId, PaymentMethod method);

    @Transactional
    Payment retryPayment(Long orderId, PaymentMethod method);

    @Transactional
    Payment partialRefund(Long orderId, double amount);

    Map<String, Double> getBill(Long orderId);
}