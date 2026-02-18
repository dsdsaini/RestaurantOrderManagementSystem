package com.Restaurant.RestaurantOrderManagementSystem.controller;

import com.Restaurant.RestaurantOrderManagementSystem.entities.Payment;
import com.Restaurant.RestaurantOrderManagementSystem.enums.PaymentMethod;
import com.Restaurant.RestaurantOrderManagementSystem.service.impl.PaymentServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST Controller for handling payments.
 * Supports processing payments, retries, partial refunds, and bill retrieval.
 */
@RestController
@RequestMapping("/payments")
@Tag(name = "Payment Controller", description = "Endpoints for handling order payments")
public class PaymentController {

    private final PaymentServiceImpl paymentService;

    public PaymentController(PaymentServiceImpl paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Process a payment for an order using a specified payment method.
     *
     * @param orderId Order ID to pay
     * @param method  Payment method (CREDIT_CARD, DEBIT_CARD, UPI, CASH)
     * @return Payment object containing status and transaction details
     */
    @PostMapping("/{orderId}/{method}")
    @Operation(summary = "Process Payment", description = "Processes payment for the given order using the selected payment method")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment processed successfully"),
            @ApiResponse(responseCode = "400", description = "Payment failed or invalid input")
    })
    public Payment pay(@PathVariable Long orderId,
                       @PathVariable PaymentMethod method) {
        return paymentService.processPayment(orderId, method);
    }

    /**
     * Retry a failed payment for an order using a specified payment method.
     *
     * @param orderId Order ID to retry payment for
     * @param method  Payment method
     * @return Payment object with updated status
     */
    @PostMapping("/retry/{orderId}/{method}")
    @Operation(summary = "Retry Payment", description = "Retries a previously failed payment for the given order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment retried successfully"),
            @ApiResponse(responseCode = "400", description = "Payment retry failed")
    })
    public Payment retry(@PathVariable Long orderId,
                         @PathVariable PaymentMethod method) {
        return paymentService.retryPayment(orderId, method);
    }

    /**
     * Process a partial refund for an order.
     *
     * @param orderId Order ID to refund
     * @param amount  Amount to refund
     * @return Payment object representing the refund transaction
     */
    @PostMapping("/refund/{orderId}")
    @Operation(summary = "Partial Refund", description = "Processes a partial refund for a given order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Refund processed successfully"),
            @ApiResponse(responseCode = "400", description = "Refund failed or invalid amount")
    })
    public Payment refund(@PathVariable Long orderId,
                          @RequestParam double amount) {
        return paymentService.partialRefund(orderId, amount);
    }

    /**
     * Retrieve the detailed bill for an order.
     *
     * @param orderId Order ID to fetch the bill
     * @return Map containing items total, tax, delivery charge, grand total, paid amount, and remaining amount
     */
    @GetMapping("/bill/{orderId}")
    @Operation(summary = "Get Bill", description = "Retrieves the detailed bill for the given order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bill retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Order not found")
    })
    public Map<String, Double> bill(@PathVariable Long orderId) {
        return paymentService.getBill(orderId);
    }
}
