package com.Restaurant.RestaurantOrderManagementSystem.service.impl;

import com.Restaurant.RestaurantOrderManagementSystem.entities.Order;
import com.Restaurant.RestaurantOrderManagementSystem.entities.Payment;
import com.Restaurant.RestaurantOrderManagementSystem.enums.PaymentMethod;
import com.Restaurant.RestaurantOrderManagementSystem.enums.PaymentStatus;
import com.Restaurant.RestaurantOrderManagementSystem.exception.PaymentException;
import com.Restaurant.RestaurantOrderManagementSystem.repository.OrderRepository;
import com.Restaurant.RestaurantOrderManagementSystem.repository.PaymentRepository;
import com.Restaurant.RestaurantOrderManagementSystem.service.PaymentService;
import com.Restaurant.RestaurantOrderManagementSystem.service.strategy.PaymentStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Implementation of {@link PaymentService} to handle payments for orders.
 * <p>
 * Supports multiple payment methods through {@link PaymentStrategy}.
 * Provides functionalities for processing payments, retrying, partial refunds,
 * and generating order bills.
 * </p>
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final OrderRepository orderRepo;
    private final PaymentRepository paymentRepo;
    private final Map<String, PaymentStrategy> strategies;

    /**
     * Constructor to inject required repositories and payment strategies.
     *
     * @param orderRepo   Repository for Order entities
     * @param paymentRepo Repository for Payment entities
     * @param strategies  Map of payment method name to {@link PaymentStrategy}
     */
    public PaymentServiceImpl(OrderRepository orderRepo, PaymentRepository paymentRepo, Map<String, PaymentStrategy> strategies) {
        this.orderRepo = orderRepo;
        this.paymentRepo = paymentRepo;
        this.strategies = strategies;
    }

    /**
     * Processes a payment for a given order using the specified payment method.
     *
     * @param orderId ID of the order
     * @param method  Payment method to use
     * @return Saved {@link Payment} object
     * @throws PaymentException if order is not found, already paid, fully paid,
     *                          or the payment method is unsupported
     */
    @Transactional
    @Override
    public Payment processPayment(Long orderId, PaymentMethod method) {

        // Fetch order and lock for update
        Order order = orderRepo.findByIdForUpdate(orderId)
                .orElseThrow(() -> new PaymentException("Order not found"));

        // Check if already paid
        if (paymentRepo.existsByOrderIdAndStatus(orderId, PaymentStatus.SUCCESS)) {
            throw new PaymentException("Order already paid");
        }

        if (order.getPaidAmount() >= order.getTotalAmount()) {
            throw new PaymentException("Order already fully paid");
        }

        double remaining = order.getTotalAmount() - order.getPaidAmount();

        // Get strategy based on payment method
        PaymentStrategy strategy = strategies.get(method.name());
        if (strategy == null) {
            throw new PaymentException("Unsupported payment method: " + method);
        }

        // Execute payment via strategy
        boolean success = strategy.pay(orderId, remaining);

        // Create payment entity
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setAmount(remaining);
        payment.setMethod(method);
        payment.setCreatedAt(LocalDateTime.now());

        if (success) {
            payment.setStatus(PaymentStatus.SUCCESS);
            order.setPaidAmount(order.getPaidAmount() + remaining);
            orderRepo.save(order); // Persist updated paid amount
            log.info("Payment success for order {}", orderId);
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            log.warn("Payment failed for order {}", orderId);
        }

        return paymentRepo.save(payment);
    }

    /**
     * Retries payment for a given order using the specified payment method.
     * Delegates to {@link #processPayment(Long, PaymentMethod)}.
     *
     * @param orderId ID of the order
     * @param method  Payment method to retry
     * @return {@link Payment} object after retry
     */
    @Transactional
    @Override
    public Payment retryPayment(Long orderId, PaymentMethod method) {
        log.info("Retrying payment for order {}", orderId);
        return processPayment(orderId, method);
    }

    /**
     * Processes a partial refund for a given order.
     *
     * @param orderId ID of the order
     * @param amount  Amount to refund
     * @return {@link Payment} object representing the refund
     * @throws PaymentException if order not found, amount invalid, or exceeds paid amount
     */
    @Transactional
    @Override
    public Payment partialRefund(Long orderId, double amount) {

        // Fetch order and lock for update
        Order order = orderRepo.findByIdForUpdate(orderId)
                .orElseThrow(() -> new PaymentException("Order not found"));

        if (amount <= 0) {
            throw new PaymentException("Refund amount must be positive");
        }

        if (amount > order.getPaidAmount()) {
            throw new PaymentException("Refund exceeds paid amount");
        }

        // Create refund payment entity
        Payment refund = new Payment();
        refund.setOrderId(orderId);
        refund.setAmount(amount);
        refund.setMethod(PaymentMethod.CASH); // Refund assumed in cash
        refund.setStatus(PaymentStatus.REFUNDED);
        refund.setRefundedAmount(amount);
        refund.setCreatedAt(LocalDateTime.now());

        // Deduct refunded amount from order
        order.setPaidAmount(order.getPaidAmount() - amount);
        orderRepo.save(order);

        log.info("Refund processed for order {} amount {}", orderId, amount);

        return paymentRepo.save(refund);
    }

    /**
     * Generates a detailed bill for a given order.
     *
     * @param orderId ID of the order
     * @return Map of bill components:
     * <ul>
     *     <li>itemsTotal - Sum of all menu items</li>
     *     <li>tax - 18% of itemsTotal</li>
     *     <li>deliveryCharge - Calculated delivery charge</li>
     *     <li>grandTotal - Total order amount</li>
     *     <li>paidAmount - Amount already paid</li>
     *     <li>remainingAmount - Remaining amount to pay</li>
     * </ul>
     * @throws PaymentException if order not found
     */
    @Override
    public Map<String, Double> getBill(Long orderId) {

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new PaymentException("Order not found"));

        // Calculate total for items
        double itemsTotal = order.getItems()
                .stream()
                .mapToDouble(i -> i.getMenuItem().getPrice() * i.getQuantity())
                .sum();

        double tax = itemsTotal * 0.18;
        double delivery = order.getTotalAmount() - (itemsTotal + tax);

        Map<String, Double> bill = new LinkedHashMap<>();
        bill.put("itemsTotal", itemsTotal);
        bill.put("tax", tax);
        bill.put("deliveryCharge", Math.max(delivery, 0));
        bill.put("grandTotal", order.getTotalAmount());
        bill.put("paidAmount", order.getPaidAmount());
        bill.put("remainingAmount", order.getTotalAmount() - order.getPaidAmount());

        return bill;
    }
}
