package com.Restaurant.RestaurantOrderManagementSystem.Service;

import com.Restaurant.RestaurantOrderManagementSystem.exception.PaymentException;
import com.Restaurant.RestaurantOrderManagementSystem.service.impl.DebitCardPayment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DebitCardPaymentTest {

    private DebitCardPayment debitCardPayment;

    @BeforeEach
    void setUp() {
        debitCardPayment = new DebitCardPayment();
    }

    @Test
    void pay_ShouldReturnTrue_WhenValidAmount() {
        boolean result = debitCardPayment.pay(101L, 750.0);

        assertTrue(result);
    }

    @Test
    void pay_ShouldThrowException_WhenAmountZero() {
        PaymentException ex = assertThrows(
                PaymentException.class,
                () -> debitCardPayment.pay(101L, 0)
        );

        assertEquals("Amount must be greater than zero for order 101", ex.getMessage());
    }

    @Test
    void pay_ShouldThrowException_WhenAmountNegative() {
        assertThrows(
                PaymentException.class,
                () -> debitCardPayment.pay(101L, -10)
        );
    }

    @Test
    void refund_ShouldReturnTrue_WhenValidAmount() {
        boolean result = debitCardPayment.refund(202L, 300);

        assertTrue(result);
    }

    @Test
    void refund_ShouldThrowException_WhenAmountInvalid() {
        PaymentException ex = assertThrows(
                PaymentException.class,
                () -> debitCardPayment.refund(202L, 0)
        );

        assertEquals("Amount must be greater than zero for order 202", ex.getMessage());
    }
}

