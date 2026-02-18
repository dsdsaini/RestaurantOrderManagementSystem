package com.Restaurant.RestaurantOrderManagementSystem.service.impl;

import com.Restaurant.RestaurantOrderManagementSystem.entities.Branch;
import com.Restaurant.RestaurantOrderManagementSystem.entities.MenuItem;
import com.Restaurant.RestaurantOrderManagementSystem.entities.OrderItem;
import com.Restaurant.RestaurantOrderManagementSystem.entities.Order;
import com.Restaurant.RestaurantOrderManagementSystem.enums.OrderStatus;
import com.Restaurant.RestaurantOrderManagementSystem.exception.BusinessException;
import com.Restaurant.RestaurantOrderManagementSystem.repository.BranchRepository;
import com.Restaurant.RestaurantOrderManagementSystem.repository.MenuItemRepository;
import com.Restaurant.RestaurantOrderManagementSystem.repository.OrderRepository;
import com.Restaurant.RestaurantOrderManagementSystem.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link OrderService} for managing restaurant orders.
 * <p>
 * Provides order creation, status updates, and saving functionality.
 * Applies business validations such as branch activity status and menu availability.
 * </p>
 */
@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepo;
    private final MenuItemRepository menuRepo;
    private final BranchRepository branchRepo;

    /**
     * Constructor to initialize repositories.
     *
     * @param orderRepo Repository for Order entities
     * @param menuRepo  Repository for MenuItem entities
     * @param branchRepo Repository for Branch entities
     */
    public OrderServiceImpl(OrderRepository orderRepo, MenuItemRepository menuRepo, BranchRepository branchRepo) {
        this.orderRepo = orderRepo;
        this.menuRepo = menuRepo;
        this.branchRepo = branchRepo;
    }

    /**
     * Creates a new order with items, instructions, and delivery charge.
     *
     * @param branchId       ID of the branch where order is placed
     * @param customerName   Name of the customer
     * @param items          Map of menu item IDs to quantities
     * @param instructions   Map of menu item IDs to special instructions (optional)
     * @param deliveryCharge Delivery charge for the order
     * @return Created {@link Order} object
     * @throws BusinessException if branch is inactive or any menu item is unavailable
     */
    @Override
    @Transactional
    public Order createOrder(Long branchId, String customerName, Map<Long, Integer> items,
                             Map<Long, String> instructions, double deliveryCharge) {

        // Fetch branch and validate its status
        Branch branch = branchRepo.findById(branchId)
                .orElseThrow(() -> new BusinessException("Branch not found"));

        if (!branch.isActive()) {
            throw new BusinessException("Branch is closed");
        }

        // Initialize order entity
        Order order = new Order();
        order.setCustomerName(customerName);
        order.setBranch(branch);
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();
        double subtotal = 0;

        // Iterate through requested items and validate availability
        for (Map.Entry<Long, Integer> entry : items.entrySet()) {
            MenuItem menuItem = menuRepo.findById(entry.getKey())
                    .orElseThrow(() -> new BusinessException("Menu item not found: " + entry.getKey()));

            if (!menuItem.isAvailable()) {
                throw new BusinessException("Item unavailable: " + menuItem.getName());
            }

            // Create individual order item
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setMenuItem(menuItem);
            orderItem.setQuantity(entry.getValue());
            orderItem.setCancelled(false);
            orderItem.setSpecialInstructions(instructions.getOrDefault(entry.getKey(), ""));
            orderItems.add(orderItem);

            // Accumulate subtotal
            subtotal += menuItem.getPrice() * entry.getValue();
        }

        order.setItems(orderItems);

        // Apply tax and delivery charge
        double tax = subtotal * 0.18; // 18% GST or service tax
        order.setTotalAmount(subtotal + tax + deliveryCharge);

        log.info("Created order for customer {} at branch {}. Total: {}", customerName, branchId, order.getTotalAmount());

        return orderRepo.save(order);
    }

    /**
     * Updates the status of an order.
     *
     * @param orderId ID of the order to update
     * @param status  New status (e.g., CREATED, COMPLETED)
     * @return Updated {@link Order} object
     * @throws BusinessException if order not found or status is invalid
     */
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

    /**
     * Saves the order entity. Used for persisting changes manually.
     *
     * @param order {@link Order} object to save
     * @return Saved {@link Order} object
     */
    @Override
    public Order saveOrder(Order order) {
        return orderRepo.save(order);
    }

    /**
     * Updates order status by {@link OrderStatus} enum.
     *
     * @param id          ID of the order
     * @param orderStatus New {@link OrderStatus}
     * @throws BusinessException if order not found
     */
    @Override
    public void updateOrderStatus(Long id, OrderStatus orderStatus) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new BusinessException("Order not found with id: " + id));
        order.setStatus(orderStatus);
        orderRepo.save(order);
    }
}
