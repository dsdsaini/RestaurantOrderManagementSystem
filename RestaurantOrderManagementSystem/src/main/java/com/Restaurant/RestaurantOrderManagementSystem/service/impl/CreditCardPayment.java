package com.Restaurant.RestaurantOrderManagementSystem.service.impl;

import com.Restaurant.RestaurantOrderManagementSystem.exception.PaymentException;
import com.Restaurant.RestaurantOrderManagementSystem.service.strategy.PaymentStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link PaymentStrategy} for handling Credit Card payments.
 * <p>
 * This service handles both payment and refund operations for credit card transactions.
 * Includes retry mechanism for robustness in case of transient gateway failures.
 * </p>
 */
@Service("CREDIT_CARD")
public class CreditCardPayment implements PaymentStrategy {

    private static final Logger log = LoggerFactory.getLogger(CreditCardPayment.class);

    // Maximum number of retry attempts for payment
    private static final int MAX_RETRIES = 3;

    /**
     * Process a credit card payment for a given order.
     *
     * @param orderId ID of the order to pay
     * @param amount  Amount to pay
     * @return true if payment succeeds
     * @throws PaymentException if amount is invalid or payment fails
     */
    @Override
    public boolean pay(Long orderId, double amount) {
        // Validate input amount
        validateAmount(orderId, amount);

        int attempt = 0;
        boolean success = false;

        // Retry loop in case of temporary gateway failures
        while (attempt < MAX_RETRIES && !success) {
            attempt++;
            try {
                log.info("Credit Card payment attempt {} for order {} amount {}", attempt, orderId, amount);
                // Simulate success from payment gateway
                success = true;
            } catch (Exception e) {
                log.error("Credit Card payment failed attempt {} for order {}: {}", attempt, orderId, e.getMessage());
            }
        }

        // Throw exception if all attempts fail
        if (!success) {
            throw new PaymentException("Credit Card payment failed for order " + orderId);
        }

        log.info("Credit Card payment successful for order {}", orderId);
        return true;
    }

    /**
     * Process a refund for a credit card payment.
     *
     * @param orderId ID of the order to refund
     * @param amount  Amount to refund
     * @return true if refund succeeds
     * @throws PaymentException if amount is invalid
     */
    @Override
    public boolean refund(Long orderId, double amount) {
        // Validate refund amount
        validateAmount(orderId, amount);

        log.info("Credit Card refund processed for order {} amount {}", orderId, amount);

        // Refund logic simulated as successful
        return true;
    }

    /**
     * Validate that the payment or refund amount is greater than zero.
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
