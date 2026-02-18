package com.Restaurant.RestaurantOrderManagementSystem.exception;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}