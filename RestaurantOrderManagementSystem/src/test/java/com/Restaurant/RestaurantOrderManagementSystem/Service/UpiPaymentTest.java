package com.Restaurant.RestaurantOrderManagementSystem.Service;

import com.Restaurant.RestaurantOrderManagementSystem.exception.PaymentException;
import com.Restaurant.RestaurantOrderManagementSystem.service.impl.UpiPayment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpiPaymentTest {

    private UpiPayment upiPayment;

    @BeforeEach
    void setUp() {
        upiPayment = new UpiPayment();
    }

    // ---------------- PAY TESTS ----------------

    @Test
    void pay_successfulPayment_returnsTrue() {
        Long orderId = 1L;
        double amount = 500.0;

        boolean result = upiPayment.pay(orderId, amount);

        assertTrue(result);
    }

    @Test
    void pay_zeroAmount_throwsException() {
        Long orderId = 1L;
        double amount = 0.0;

        PaymentException ex = assertThrows(
                PaymentException.class,
                () -> upiPayment.pay(orderId, amount)
        );

        assertEquals("Amount must be greater than zero for order " + orderId, ex.getMessage());
    }

    @Test
    void pay_negativeAmount_throwsException() {
        Long orderId = 1L;
        double amount = -10.0;

        assertThrows(PaymentException.class,
                () -> upiPayment.pay(orderId, amount));
    }

    // ---------------- REFUND TESTS ----------------

    @Test
    void refund_successfulRefund_returnsTrue() {
        Long orderId = 2L;
        double amount = 200.0;

        boolean result = upiPayment.refund(orderId, amount);

        assertTrue(result);
    }

    @Test
    void refund_invalidAmount_throwsException() {
        Long orderId = 2L;
        double amount = -50.0;

        assertThrows(PaymentException.class,
                () -> upiPayment.refund(orderId, amount));
    }
}

