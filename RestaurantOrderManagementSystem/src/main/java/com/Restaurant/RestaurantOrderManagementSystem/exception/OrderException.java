package com.Restaurant.RestaurantOrderManagementSystem.exception;

public class OrderException extends RuntimeException {
    public OrderException(String message, Exception ex) {
        super(message);
    }
}

