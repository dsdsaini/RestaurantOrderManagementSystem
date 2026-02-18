package com.Restaurant.RestaurantOrderManagementSystem.service.impl;

import com.Restaurant.RestaurantOrderManagementSystem.exception.PaymentException;
import com.Restaurant.RestaurantOrderManagementSystem.service.strategy.PaymentStrategy;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("DEBIT_CARD")
public class DebitCardPayment implements PaymentStrategy {
    private static final Logger log = LoggerFactory.getLogger(DebitCardPayment.class);

    private static final int MAX_RETRIES = 3;

    @Override
    public boolean pay(Long orderId, double amount) {
        validateAmount(orderId, amount);

        int attempt = 0;
        boolean success = false;
        while (attempt < MAX_RETRIES && !success) {
            attempt++;
            try {
                log.info("Debit Card payment attempt {} for order {} amount {}", attempt, orderId, amount);
                success = true;
            } catch (Exception e) {
                log.error("Debit Card payment failed attempt {} for order {}: {}", attempt, orderId, e.getMessage());
            }
        }

        if (!success) throw new PaymentException("Debit Card payment failed for order " + orderId);
        log.info("Debit Card payment successful for order {}", orderId);
        return true;
    }

    @Override
    public boolean refund(Long orderId, double amount) {
        validateAmount(orderId, amount);
        log.info("Debit Card refund processed for order {} amount {}", orderId, amount);
        return true;
    }

    private void validateAmount(Long orderId, double amount) {
        if (amount <= 0)
            throw new PaymentException("Amount must be greater than zero for order " + orderId);
    }
}
