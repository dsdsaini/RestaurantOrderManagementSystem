package com.Restaurant.RestaurantOrderManagementSystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MenuException.class)
    public ResponseEntity<String> handleMenu(MenuException ex) {
        return ResponseEntity.badRequest().body("Menu Exception: " +ex.getMessage());
    }

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<String> handleOrder(OrderException ex) {
        return ResponseEntity.badRequest().body("Order Exception: " +ex.getMessage());
    }
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handleBusiness(BusinessException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Business Error: " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error: " + ex.getMessage());
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<String> handlePayment(PaymentException ex) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Payment Error: " + ex.getMessage());
    }
}
