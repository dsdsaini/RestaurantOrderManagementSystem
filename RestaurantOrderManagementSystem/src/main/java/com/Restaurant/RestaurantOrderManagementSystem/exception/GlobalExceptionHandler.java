package com.Restaurant.RestaurantOrderManagementSystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for the application.
 *
 * <p>This class intercepts exceptions thrown by controllers and services
 * and converts them into standardized HTTP responses with meaningful messages.</p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles exceptions of type {@link MenuException}.
     *
     * @param ex the MenuException thrown
     * @return ResponseEntity with status 400 (Bad Request) and error message
     */
    @ExceptionHandler(MenuException.class)
    public ResponseEntity<String> handleMenu(MenuException ex) {
        return ResponseEntity
                .badRequest()
                .body("Menu Exception: " + ex.getMessage());
    }

    /**
     * Handles exceptions of type {@link OrderException}.
     *
     * @param ex the OrderException thrown
     * @return ResponseEntity with status 400 (Bad Request) and error message
     */
    @ExceptionHandler(OrderException.class)
    public ResponseEntity<String> handleOrder(OrderException ex) {
        return ResponseEntity
                .badRequest()
                .body("Order Exception: " + ex.getMessage());
    }

    /**
     * Handles exceptions of type {@link BusinessException}.
     *
     * @param ex the BusinessException thrown
     * @return ResponseEntity with status 400 (Bad Request) and error message
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handleBusiness(BusinessException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Business Error: " + ex.getMessage());
    }

    /**
     * Handles all generic exceptions not explicitly handled by other handlers.
     *
     * @param ex the generic Exception thrown
     * @return ResponseEntity with status 500 (Internal Server Error) and error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneric(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Server Error: " + ex.getMessage());
    }

    /**
     * Handles exceptions of type {@link PaymentException}.
     *
     * @param ex the PaymentException thrown
     * @return ResponseEntity with status 406 (Not Acceptable) and error message
     */
    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<String> handlePayment(PaymentException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body("Payment Error: " + ex.getMessage());
    }
}

