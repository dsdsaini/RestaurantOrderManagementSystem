package com.Restaurant.RestaurantOrderManagementSystem.service.impl;

import com.Restaurant.RestaurantOrderManagementSystem.exception.PaymentException;
import com.Restaurant.RestaurantOrderManagementSystem.service.strategy.PaymentStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link PaymentStrategy} for UPI payments.
 * <p>
 * Supports payment processing and refund operations via UPI.
 * Includes retry mechanism for failed attempts (up to 3 attempts).
 * </p>
 */
@Service("UPI")
public class UpiPayment implements PaymentStrategy {

    private static final Logger log = LoggerFactory.getLogger(UpiPayment.class);

    /** Maximum number of retries for a failed payment or refund attempt */
    private static final int MAX_RETRIES = 3;

    /**
     * Processes a UPI payment for a given order and amount.
     *
     * @param orderId ID of the order
     * @param amount  Amount to pay
     * @return true if payment is successful
     * @throws PaymentException if amount is invalid or payment fails after retries
     */
    @Override
    public boolean pay(Long orderId, double amount) {
        // Validate the payment amount before processing
        validateAmount(orderId, amount);

        int attempt = 0;
        boolean success = false;

        // Retry loop for up to MAX_RETRIES attempts
        while (attempt < MAX_RETRIES && !success) {
            attempt++;
            try {
                log.info("UPI payment attempt {} for order {} amount {}", attempt, orderId, amount);
                // Simulate payment success (replace with actual gateway call)
                success = true;
            } catch (Exception e) {
                log.error("UPI payment failed attempt {} for order {}: {}", attempt, orderId, e.getMessage());
            }
        }

        // Throw exception if all attempts failed
        if (!success) throw new PaymentException("UPI payment failed for order " + orderId);

        log.info("UPI payment successful for order {}", orderId);
        return true;
    }

    /**
     * Processes a UPI refund for a given order and amount.
     *
     * @param orderId ID of the order
     * @param amount  Amount to refund
     * @return true if refund is successful
     * @throws PaymentException if amount is invalid
     */
    @Override
    public boolean refund(Long orderId, double amount) {
        // Validate the refund amount
        validateAmount(orderId, amount);

        // Log refund success (replace with actual gateway logic if needed)
        log.info("UPI refund processed for order {} amount {}", orderId, amount);

        return true;
    }

    /**
     * Validates that the amount is positive.
     *
     * @param orderId ID of the order
     * @param amount  Amount to validate
     * @throws PaymentException if amount is less than or equal to zero
     */
    private void validateAmount(Long orderId, double amount) {
        if (amount <= 0)
            throw new PaymentException("Amount must be greater than zero for order " + orderId);
    }
}
