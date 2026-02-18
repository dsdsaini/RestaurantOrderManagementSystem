package com.Restaurant.RestaurantOrderManagementSystem.contoller;

import com.Restaurant.RestaurantOrderManagementSystem.entities.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.Restaurant.RestaurantOrderManagementSystem.service.OrderService;

import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public Order createOrder(@RequestParam Long branchId,
                             @RequestParam String customerName,
                             @RequestBody Map<Long, Integer> items,
                             @RequestBody(required = false) Map<Long, String> instructions,
                             @RequestParam(defaultValue = "0") double deliveryCharge) {
        return orderService.createOrder(branchId, customerName, items, instructions, deliveryCharge);
    }

    @PutMapping("/{orderId}/status")
    public Order updateStatus(@PathVariable Long orderId, @RequestParam String status) {
        return orderService.updateStatus(orderId, status);
    }
}
