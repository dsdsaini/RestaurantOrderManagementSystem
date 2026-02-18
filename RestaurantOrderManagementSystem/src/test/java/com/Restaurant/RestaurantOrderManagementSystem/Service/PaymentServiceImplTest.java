package com.Restaurant.RestaurantOrderManagementSystem.Service;

import com.Restaurant.RestaurantOrderManagementSystem.entities.MenuItem;
import com.Restaurant.RestaurantOrderManagementSystem.entities.Order;
import com.Restaurant.RestaurantOrderManagementSystem.entities.OrderItem;
import com.Restaurant.RestaurantOrderManagementSystem.entities.Payment;
import com.Restaurant.RestaurantOrderManagementSystem.enums.PaymentMethod;
import com.Restaurant.RestaurantOrderManagementSystem.enums.PaymentStatus;
import com.Restaurant.RestaurantOrderManagementSystem.exception.PaymentException;
import com.Restaurant.RestaurantOrderManagementSystem.repository.OrderRepository;
import com.Restaurant.RestaurantOrderManagementSystem.repository.PaymentRepository;
import com.Restaurant.RestaurantOrderManagementSystem.service.impl.PaymentServiceImpl;
import com.Restaurant.RestaurantOrderManagementSystem.service.strategy.PaymentStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceImplTest {

    @Mock
    private OrderRepository orderRepo;

    @Mock
    private PaymentRepository paymentRepo;

    @Mock
    private PaymentStrategy upiStrategy;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Order order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Map<String, PaymentStrategy> strategies = new HashMap<>();
        strategies.put("UPI", upiStrategy);

        paymentService = new PaymentServiceImpl(orderRepo, paymentRepo, strategies);

        order = new Order();
        order.setId(1L);
        order.setTotalAmount(1000);
        order.setPaidAmount(0);
    }

    // ================= PROCESS PAYMENT =================

    @Test
    void processPayment_success() {
        when(orderRepo.findByIdForUpdate(1L)).thenReturn(Optional.of(order));
        when(paymentRepo.existsByOrderIdAndStatus(1L, PaymentStatus.SUCCESS)).thenReturn(false);
        when(upiStrategy.pay(1L, 1000)).thenReturn(true);
        when(paymentRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Payment payment = paymentService.processPayment(1L, PaymentMethod.UPI);

        assertEquals(PaymentStatus.SUCCESS, payment.getStatus());
        assertEquals(1000, payment.getAmount());
        verify(orderRepo).save(order);
        assertEquals(1000, order.getPaidAmount());
    }

    @Test
    void processPayment_orderNotFound() {
        when(orderRepo.findByIdForUpdate(1L)).thenReturn(Optional.empty());

        assertThrows(PaymentException.class,
                () -> paymentService.processPayment(1L, PaymentMethod.UPI));
    }

    @Test
    void processPayment_alreadyPaid() {
        when(orderRepo.findByIdForUpdate(1L)).thenReturn(Optional.of(order));
        when(paymentRepo.existsByOrderIdAndStatus(1L, PaymentStatus.SUCCESS)).thenReturn(true);

        assertThrows(PaymentException.class,
                () -> paymentService.processPayment(1L, PaymentMethod.UPI));
    }

    @Test
    void processPayment_fullyPaid() {
        order.setPaidAmount(1000);

        when(orderRepo.findByIdForUpdate(1L)).thenReturn(Optional.of(order));
        when(paymentRepo.existsByOrderIdAndStatus(1L, PaymentStatus.SUCCESS)).thenReturn(false);

        assertThrows(PaymentException.class,
                () -> paymentService.processPayment(1L, PaymentMethod.UPI));
    }

    @Test
    void processPayment_unsupportedMethod() {
        when(orderRepo.findByIdForUpdate(1L)).thenReturn(Optional.of(order));
        when(paymentRepo.existsByOrderIdAndStatus(1L, PaymentStatus.SUCCESS)).thenReturn(false);

        PaymentServiceImpl service =
                new PaymentServiceImpl(orderRepo, paymentRepo, new HashMap<>());

        assertThrows(PaymentException.class,
                () -> service.processPayment(1L, PaymentMethod.UPI));
    }

    // ================= RETRY =================

    @Test
    void retryPayment_callsProcessPayment() {
        PaymentServiceImpl spyService = Mockito.spy(paymentService);

        doReturn(new Payment()).when(spyService).processPayment(1L, PaymentMethod.UPI);

        Payment p = spyService.retryPayment(1L, PaymentMethod.UPI);

        assertNotNull(p);
        verify(spyService).processPayment(1L, PaymentMethod.UPI);
    }

    // ================= REFUND =================

    @Test
    void partialRefund_success() {
        order.setPaidAmount(500);

        when(orderRepo.findByIdForUpdate(1L)).thenReturn(Optional.of(order));
        when(paymentRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Payment refund = paymentService.partialRefund(1L, 200);

        assertEquals(PaymentStatus.REFUNDED, refund.getStatus());
        assertEquals(200, refund.getAmount());
        assertEquals(300, order.getPaidAmount());
        verify(orderRepo).save(order);
    }

    @Test
    void partialRefund_amountTooHigh() {
        order.setPaidAmount(100);

        when(orderRepo.findByIdForUpdate(1L)).thenReturn(Optional.of(order));

        assertThrows(PaymentException.class,
                () -> paymentService.partialRefund(1L, 200));
    }

    @Test
    void partialRefund_negativeAmount() {
        order.setPaidAmount(100);

        when(orderRepo.findByIdForUpdate(1L)).thenReturn(Optional.of(order));

        assertThrows(PaymentException.class,
                () -> paymentService.partialRefund(1L, -10));
    }

    // ================= BILL =================

    @Test
    void getBill_success() {
        MenuItem item = new MenuItem();
        item.setPrice(100);

        OrderItem oi = new OrderItem();
        oi.setMenuItem(item);
        oi.setQuantity(2);

        order.setItems(List.of(oi));
        order.setTotalAmount(236); // 200 + 36 tax
        order.setPaidAmount(100);

        when(orderRepo.findById(1L)).thenReturn(Optional.of(order));

        Map<String, Double> bill = paymentService.getBill(1L);

        assertEquals(200, bill.get("itemsTotal"));
        assertEquals(36, bill.get("tax"));
        assertEquals(236, bill.get("grandTotal"));
        assertEquals(100, bill.get("paidAmount"));
        assertEquals(136, bill.get("remainingAmount"));
    }

    @Test
    void getBill_orderNotFound() {
        when(orderRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PaymentException.class,
                () -> paymentService.getBill(1L));
    }
}
