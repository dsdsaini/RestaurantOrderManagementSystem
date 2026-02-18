package com.Restaurant.RestaurantOrderManagementSystem.service.strategy;

public interface PaymentStrategy {
    /**
     * Process payment
     * @param orderId - Order to pay for
     * @param amount - Amount to pay
     * @return true if payment succeeds
     */
    boolean pay(Long orderId, double amount);

    /**
     * Refund payment
     * @param orderId - Order to refund
     * @param amount - Amount to refund
     * @return true if refund succeeds
     */
    boolean refund(Long orderId, double amount);
}
