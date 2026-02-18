package com.Restaurant.RestaurantOrderManagementSystem.service.impl;

import com.Restaurant.RestaurantOrderManagementSystem.exception.PaymentException;
import com.Restaurant.RestaurantOrderManagementSystem.service.strategy.PaymentStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link PaymentStrategy} for handling cash payments.
 * <p>
 * This service handles both payment and refund operations for cash transactions.
 * Cash payments are immediate and always succeed unless the amount is invalid.
 * </p>
 */
@Service("CASH")
public class CashPayment implements PaymentStrategy {

    private static final Logger log = LoggerFactory.getLogger(CashPayment.class);

    // Maximum number of retry attempts for payment or refund
    private static final int MAX_RETRIES = 3;

    /**
     * Process a cash payment for a given order.
     *
     * @param orderId ID of the order to pay
     * @param amount  Amount to pay
     * @return true if payment succeeds
     * @throws PaymentException if amount is invalid or payment fails
     */
    @Override
    public boolean pay(Long orderId, double amount) {
        // Validate the payment amount
        validateAmount(orderId, amount);

        int attempt = 0;
        boolean success = false;

        // Retry loop for robustness (even though cash is immediate)
        while (attempt < MAX_RETRIES && !success) {
            attempt++;
            try {
                log.info("Cash payment attempt {} for order {} amount {}", attempt, orderId, amount);
                success = true; // Cash payment always succeeds
            } catch (Exception e) {
                log.error("Cash payment failed attempt {} for order {}: {}", attempt, orderId, e.getMessage());
            }
        }

        // Throw exception if all attempts fail
        if (!success) {
            throw new PaymentException("Cash payment failed for order " + orderId);
        }

        log.info("Cash payment successful for order {}", orderId);
        return true;
    }

    /**
     * Process a cash refund for a given order.
     *
     * @param orderId ID of the order to refund
     * @param amount  Amount to refund
     * @return true if refund succeeds
     * @throws PaymentException if amount is invalid or refund fails
     */
    @Override
    public boolean refund(Long orderId, double amount) {
        // Validate the refund amount
        validateAmount(orderId, amount);

        int attempt = 0;
        boolean success = false;

        // Retry loop for refund
        while (attempt < MAX_RETRIES && !success) {
            attempt++;
            try {
                log.info("Cash refund attempt {} for order {} amount {}", attempt, orderId, amount);
                success = true; // Cash refund always succeeds
            } catch (Exception e) {
                log.error("Cash refund failed attempt {} for order {}: {}", attempt, orderId, e.getMessage());
            }
        }

        // Throw exception if all attempts fail
        if (!success) {
            throw new PaymentException("Cash refund failed for order " + orderId);
        }

        log.info("Cash refund successful for order {}", orderId);
        return true;
    }

    /**
     * Validate that the amount for payment or refund is positive.
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
