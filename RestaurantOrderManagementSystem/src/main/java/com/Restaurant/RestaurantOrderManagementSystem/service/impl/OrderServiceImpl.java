package com.Restaurant.RestaurantOrderManagementSystem.service.impl;

import com.Restaurant.RestaurantOrderManagementSystem.entities.Branch;
import com.Restaurant.RestaurantOrderManagementSystem.entities.MenuItem;
import com.Restaurant.RestaurantOrderManagementSystem.entities.OrderItem;
import com.Restaurant.RestaurantOrderManagementSystem.enums.OrderStatus;
import com.Restaurant.RestaurantOrderManagementSystem.entities.Order;
import com.Restaurant.RestaurantOrderManagementSystem.exception.BusinessException;
import com.Restaurant.RestaurantOrderManagementSystem.repository.BranchRepository;
import com.Restaurant.RestaurantOrderManagementSystem.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.Restaurant.RestaurantOrderManagementSystem.repository.OrderRepository;
import com.Restaurant.RestaurantOrderManagementSystem.service.OrderService;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepo;
    private final MenuItemRepository menuRepo;
    private final BranchRepository branchRepo;

    public OrderServiceImpl(OrderRepository orderRepo, MenuItemRepository menuRepo, BranchRepository branchRepo) {
        this.orderRepo = orderRepo;
        this.menuRepo = menuRepo;
        this.branchRepo = branchRepo;
    }

    @Override
    @Transactional
    public Order createOrder(Long branchId, String customerName, Map<Long, Integer> items, Map<Long, String> instructions, double deliveryCharge) {

        Branch branch = branchRepo.findById(branchId)
                .orElseThrow(() -> new BusinessException("Branch not found"));

        if (!branch.isActive()) {
            throw new BusinessException("Branch is closed");
        }

        Order order = new Order();
        order.setCustomerName(customerName);
        order.setBranch(branch);
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();
        double subtotal = 0;

        for (Map.Entry<Long, Integer> entry : items.entrySet()) {
            MenuItem menuItem = menuRepo.findById(entry.getKey())
                    .orElseThrow(() -> new BusinessException("Menu item not found: " + entry.getKey()));

            if (!menuItem.isAvailable()) {
                throw new BusinessException("Item unavailable: " + menuItem.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setMenuItem(menuItem);
            orderItem.setQuantity(entry.getValue());
            orderItem.setCancelled(false);
            orderItem.setSpecialInstructions(instructions.getOrDefault(entry.getKey(), ""));
            orderItems.add(orderItem);

            subtotal += menuItem.getPrice() * entry.getValue();
        }

        order.setItems(orderItems);

        // Apply combos and discounts (example: you can integrate ComboMealService here)
        // For simplicity, assume subtotal already includes any discounts

        double tax = subtotal * 0.18; // 18% service tax
        order.setTotalAmount(subtotal + tax + deliveryCharge);

        log.info("Created order for customer {} at branch {}. Total: {}", customerName, branchId, order.getTotalAmount());
        return orderRepo.save(order);
    }

    @Override
    @Transactional
    public Order updateStatus(Long orderId, String status) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new BusinessException("Order not found"));

        OrderStatus os;
        try {
            os = OrderStatus.valueOf(status.toUpperCase());
        } catch (Exception e) {
            throw new BusinessException("Invalid order status: " + status);
        }

        order.setStatus(os);
        log.info("Order {} status updated to {}", orderId, os);
        return orderRepo.save(order);
    }

    @Override
    public Order saveOrder(Order order) {
        return orderRepo.save(order);
    }

    @Override
    public void updateOrderStatus(Long id, OrderStatus orderStatus) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new BusinessException("Order not found with id: " + id));
        order.setStatus(orderStatus);
        orderRepo.save(order);
    }
}
