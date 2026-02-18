package com.Restaurant.RestaurantOrderManagementSystem.service.impl;

import com.Restaurant.RestaurantOrderManagementSystem.exception.PaymentException;
import com.Restaurant.RestaurantOrderManagementSystem.service.strategy.PaymentStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link PaymentStrategy} for handling Debit Card payments.
 * <p>
 * This service processes both payment and refund operations for debit card transactions.
 * Includes retry mechanism to handle temporary failures in the payment process.
 * </p>
 */
@Service("DEBIT_CARD")
public class DebitCardPayment implements PaymentStrategy {

    private static final Logger log = LoggerFactory.getLogger(DebitCardPayment.class);

    // Maximum retry attempts for payment or refund
    private static final int MAX_RETRIES = 3;

    /**
     * Process a debit card payment for a given order.
     *
     * @param orderId ID of the order to pay
     * @param amount  Amount to pay
     * @return true if payment succeeds
     * @throws PaymentException if the amount is invalid or all attempts fail
     */
    @Override
    public boolean pay(Long orderId, double amount) {
        // Validate input amount
        validateAmount(orderId, amount);

        int attempt = 0;
        boolean success = false;

        // Retry loop for handling temporary failures
        while (attempt < MAX_RETRIES && !success) {
            attempt++;
            try {
                log.info("Debit Card payment attempt {} for order {} amount {}", attempt, orderId, amount);
                // Simulate debit card gateway success
                success = true;
            } catch (Exception e) {
                log.error("Debit Card payment failed attempt {} for order {}: {}", attempt, orderId, e.getMessage());
            }
        }

        // Throw exception if all attempts fail
        if (!success) {
            throw new PaymentException("Debit Card payment failed for order " + orderId);
        }

        log.info("Debit Card payment successful for order {}", orderId);
        return true;
    }

    /**
     * Process a refund for a debit card payment.
     *
     * @param orderId ID of the order to refund
     * @param amount  Amount to refund
     * @return true if refund succeeds
     * @throws PaymentException if the amount is invalid
     */
    @Override
    public boolean refund(Long orderId, double amount) {
        // Validate refund amount
        validateAmount(orderId, amount);

        log.info("Debit Card refund processed for order {} amount {}", orderId, amount);
        // Refund simulated as successful
        return true;
    }

    /**
     * Validate that the payment or refund amount is positive.
     *
     * @param orderId ID of the order
     * @param amount  Amount to validate
     * @throws PaymentException if amount is less than or equal to zero
     */
    private void validateAmount(Long orderId, double amount) {
        if (amount <= 0) {
            throw new PaymentException("Amount must be greater than zero for order " + orderId);
        }
    }
}
