package com.Restaurant.RestaurantOrderManagementSystem.contoller;

import com.Restaurant.RestaurantOrderManagementSystem.entities.Order;
import com.Restaurant.RestaurantOrderManagementSystem.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST Controller for managing Orders.
 * Supports creating new orders and updating order status.
 */
@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order Controller", description = "Endpoints for managing customer orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Create a new order for a given branch and customer.
     *
     * @param branchId       Branch ID where order is placed
     * @param customerName   Customer name
     * @param items          Map of MenuItem ID and quantity
     * @param instructions   Optional map of MenuItem ID and special instructions
     * @param deliveryCharge Delivery charge for the order (default 0)
     * @return Created Order object with generated ID and calculated total
     */
    @PostMapping("/create")
    @Operation(summary = "Create Order", description = "Creates a new order for a customer at a specific branch")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid order data")
    })
    public Order createOrder(@RequestParam Long branchId,
                             @RequestParam String customerName,
                             @RequestBody Map<Long, Integer> items,
                             @RequestBody(required = false) Map<Long, String> instructions,
                             @RequestParam(defaultValue = "0") double deliveryCharge) {
        return orderService.createOrder(branchId, customerName, items, instructions, deliveryCharge);
    }

    /**
     * Update the status of an existing order.
     *
     * @param orderId Order ID
     * @param status  New status (CREATED, CONFIRMED, COMPLETED, CANCELLED, etc.)
     * @return Updated Order object
     */
    @PutMapping("/{orderId}/status")
    @Operation(summary = "Update Order Status", description = "Updates the status of an existing order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid order status or order not found")
    })
    public Order updateStatus(@PathVariable Long orderId, @RequestParam String status) {
        return orderService.updateStatus(orderId, status);
    }
}
