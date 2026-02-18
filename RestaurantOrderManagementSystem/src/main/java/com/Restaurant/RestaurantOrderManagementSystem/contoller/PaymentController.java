package com.Restaurant.RestaurantOrderManagementSystem.contoller;


import com.Restaurant.RestaurantOrderManagementSystem.entities.Payment;
import com.Restaurant.RestaurantOrderManagementSystem.enums.PaymentMethod;
import com.Restaurant.RestaurantOrderManagementSystem.service.impl.PaymentServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentServiceImpl paymentService;

    public PaymentController(PaymentServiceImpl paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/{orderId}/{method}")
    public Payment pay(@PathVariable Long orderId,
                       @PathVariable PaymentMethod method) {
        return paymentService.processPayment(orderId, method);
    }

    @PostMapping("/retry/{orderId}/{method}")
    public Payment retry(@PathVariable Long orderId,
                         @PathVariable PaymentMethod method) {
        return paymentService.retryPayment(orderId, method);
    }

    @PostMapping("/refund/{orderId}")
    public Payment refund(@PathVariable Long orderId,
                          @RequestParam double amount) {
        return paymentService.partialRefund(orderId, amount);
    }

    @GetMapping("/bill/{orderId}")
    public Map<String, Double> bill(@PathVariable Long orderId) {
        return paymentService.getBill(orderId);
    }
}
