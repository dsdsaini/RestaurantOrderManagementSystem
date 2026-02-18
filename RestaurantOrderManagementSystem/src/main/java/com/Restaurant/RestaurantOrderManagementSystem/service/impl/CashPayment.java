package com.Restaurant.RestaurantOrderManagementSystem.service.impl;

import com.Restaurant.RestaurantOrderManagementSystem.exception.PaymentException;
import com.Restaurant.RestaurantOrderManagementSystem.service.strategy.PaymentStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("CASH")

public class CashPayment implements PaymentStrategy {

    private static final Logger log = LoggerFactory.getLogger(CashPayment.class);

    private static final int MAX_RETRIES = 3;

    @Override
    public boolean pay(Long orderId, double amount) {
        validateAmount(orderId, amount);

        int attempt = 0;
        boolean success = false;
        while (attempt < MAX_RETRIES && !success) {
            attempt++;
            try {
                log.info("Cash payment attempt {} for order {} amount {}", attempt, orderId, amount);
                success = true; // Cash is always immediate
            } catch (Exception e) {
                log.error("Cash payment failed attempt {} for order {}: {}", attempt, orderId, e.getMessage());
            }
        }

        if (!success) throw new PaymentException("Cash payment failed for order " + orderId);
        log.info("Cash payment successful for order {}", orderId);
        return true;
    }

    @Override
    public boolean refund(Long orderId, double amount) {
        validateAmount(orderId, amount);

        int attempt = 0;
        boolean success = false;
        while (attempt < MAX_RETRIES && !success) {
            attempt++;
            try {
                log.info("Cash refund attempt {} for order {} amount {}", attempt, orderId, amount);
                success = true;
            } catch (Exception e) {
                log.error("Cash refund failed attempt {} for order {}: {}", attempt, orderId, e.getMessage());
            }
        }

        if (!success) throw new PaymentException("Cash refund failed for order " + orderId);
        log.info("Cash refund successful for order {}", orderId);
        return true;
    }

    private void validateAmount(Long orderId, double amount) {
        if (amount <= 0)
            throw new PaymentException("Amount must be greater than zero for order " + orderId);
    }
}