package com.Restaurant.RestaurantOrderManagementSystem.Service;

import com.Restaurant.RestaurantOrderManagementSystem.exception.PaymentException;
import com.Restaurant.RestaurantOrderManagementSystem.service.impl.CashPayment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CashPaymentTest {

    private CashPayment cashPayment;

    @BeforeEach
    void setUp() {
        cashPayment = new CashPayment();
    }

    @Test
    void pay_ShouldSucceed_WhenValidAmount() {
        boolean result = cashPayment.pay(1L, 500.0);

        assertTrue(result);
    }

    @Test
    void pay_ShouldThrowException_WhenAmountZero() {
        PaymentException ex = assertThrows(
                PaymentException.class,
                () -> cashPayment.pay(1L, 0)
        );

        assertEquals("Amount must be greater than zero for order 1", ex.getMessage());
    }

    @Test
    void pay_ShouldThrowException_WhenAmountNegative() {
        assertThrows(
                PaymentException.class,
                () -> cashPayment.pay(1L, -100)
        );
    }

    @Test
    void refund_ShouldSucceed_WhenValidAmount() {
        boolean result = cashPayment.refund(2L, 200);

        assertTrue(result);
    }

    @Test
    void refund_ShouldThrowException_WhenAmountInvalid() {
        assertThrows(
                PaymentException.class,
                () -> cashPayment.refund(2L, 0)
        );
    }
}

