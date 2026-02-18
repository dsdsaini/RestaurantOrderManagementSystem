package com.Restaurant.RestaurantOrderManagementSystem.Service;

import com.Restaurant.RestaurantOrderManagementSystem.exception.PaymentException;
import com.Restaurant.RestaurantOrderManagementSystem.service.impl.CreditCardPayment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreditCardPaymentTest {

    private CreditCardPayment creditCardPayment;

    @BeforeEach
    void setUp() {
        creditCardPayment = new CreditCardPayment();
    }

    @Test
    void pay_ShouldReturnTrue_WhenValidAmount() {
        boolean result = creditCardPayment.pay(10L, 1000.0);

        assertTrue(result);
    }

    @Test
    void pay_ShouldThrowException_WhenAmountZero() {
        PaymentException ex = assertThrows(
                PaymentException.class,
                () -> creditCardPayment.pay(10L, 0)
        );

        assertEquals("Amount must be greater than zero for order 10", ex.getMessage());
    }

    @Test
    void pay_ShouldThrowException_WhenAmountNegative() {
        assertThrows(
                PaymentException.class,
                () -> creditCardPayment.pay(10L, -500)
        );
    }

    @Test
    void refund_ShouldReturnTrue_WhenValidAmount() {
        boolean result = creditCardPayment.refund(20L, 300);

        assertTrue(result);
    }

    @Test
    void refund_ShouldThrowException_WhenAmountInvalid() {
        PaymentException ex = assertThrows(
                PaymentException.class,
                () -> creditCardPayment.refund(20L, 0)
        );

        assertEquals("Amount must be greater than zero for order 20", ex.getMessage());
    }
}

