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

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final OrderRepository orderRepo;
    private final PaymentRepository paymentRepo;
    private final Map<String, PaymentStrategy> strategies;

    public PaymentServiceImpl(OrderRepository orderRepo, PaymentRepository paymentRepo, Map<String, PaymentStrategy> strategies) {
        this.orderRepo = orderRepo;
        this.paymentRepo = paymentRepo;
        this.strategies = strategies;
    }

    @Transactional
    @Override
    public Payment processPayment(Long orderId, PaymentMethod method) {

        Order order = orderRepo.findByIdForUpdate(orderId)
                .orElseThrow(() -> new PaymentException("Order not found"));

        if (paymentRepo.existsByOrderIdAndStatus(orderId, PaymentStatus.SUCCESS)) {
            throw new PaymentException("Order already paid");
        }

        if (order.getPaidAmount() >= order.getTotalAmount()) {
            throw new PaymentException("Order already fully paid");
        }

        double remaining = order.getTotalAmount() - order.getPaidAmount();

        PaymentStrategy strategy = strategies.get(method.name());
        if (strategy == null) {
            throw new PaymentException("Unsupported payment method: " + method);
        }

        boolean success = strategy.pay(orderId, remaining);

        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setAmount(remaining);
        payment.setMethod(method);
        payment.setCreatedAt(LocalDateTime.now());

        if (success) {
            payment.setStatus(PaymentStatus.SUCCESS);
            order.setPaidAmount(order.getPaidAmount() + remaining);
            orderRepo.save(order);
            log.info("Payment success for order {}", orderId);
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            log.warn("Payment failed for order {}", orderId);
        }

        return paymentRepo.save(payment);
    }

    @Transactional
    @Override
    public Payment retryPayment(Long orderId, PaymentMethod method) {

        log.info("Retrying payment for order {}", orderId);
        return processPayment(orderId, method);
    }

    @Transactional
    @Override
    public Payment partialRefund(Long orderId, double amount) {

        Order order = orderRepo.findByIdForUpdate(orderId)
                .orElseThrow(() -> new PaymentException("Order not found"));

        if (amount <= 0) {
            throw new PaymentException("Refund amount must be positive");
        }

        if (amount > order.getPaidAmount()) {
            throw new PaymentException("Refund exceeds paid amount");
        }

        Payment refund = new Payment();
        refund.setOrderId(orderId);
        refund.setAmount(amount);
        refund.setMethod(PaymentMethod.CASH);
        refund.setStatus(PaymentStatus.REFUNDED);
        refund.setRefundedAmount(amount);
        refund.setCreatedAt(LocalDateTime.now());

        order.setPaidAmount(order.getPaidAmount() - amount);
        orderRepo.save(order);

        log.info("Refund processed for order {} amount {}", orderId, amount);

        return paymentRepo.save(refund);
    }

    @Override
    public Map<String, Double> getBill(Long orderId) {

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new PaymentException("Order not found"));

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
