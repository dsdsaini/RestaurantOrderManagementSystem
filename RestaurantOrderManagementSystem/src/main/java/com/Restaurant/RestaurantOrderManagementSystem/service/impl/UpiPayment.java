package com.Restaurant.RestaurantOrderManagementSystem.service.impl;

import com.Restaurant.RestaurantOrderManagementSystem.exception.PaymentException;
import com.Restaurant.RestaurantOrderManagementSystem.service.strategy.PaymentStrategy;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("UPI")
public class UpiPayment implements PaymentStrategy {

    private static final Logger log = LoggerFactory.getLogger(UpiPayment.class);

    private static final int MAX_RETRIES = 3;

    @Override
    public boolean pay(Long orderId, double amount) {
        validateAmount(orderId, amount);

        int attempt = 0;
        boolean success = false;
        while (attempt < MAX_RETRIES && !success) {
            attempt++;
            try {
                log.info("UPI payment attempt {} for order {} amount {}", attempt, orderId, amount);
                success = true;
            } catch (Exception e) {
                log.error("UPI payment failed attempt {} for order {}: {}", attempt, orderId, e.getMessage());
            }
        }

        if (!success) throw new PaymentException("UPI payment failed for order " + orderId);
        log.info("UPI payment successful for order {}", orderId);
        return true;
    }

    @Override
    public boolean refund(Long orderId, double amount) {
        validateAmount(orderId, amount);
        log.info("UPI refund processed for order {} amount {}", orderId, amount);
        return true;
    }

    private void validateAmount(Long orderId, double amount) {
        if (amount <= 0)
            throw new PaymentException("Amount must be greater than zero for order " + orderId);
    }
}