package com.Restaurant.RestaurantOrderManagementSystem.entities;

import com.Restaurant.RestaurantOrderManagementSystem.enums.PaymentStatus;
import com.Restaurant.RestaurantOrderManagementSystem.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Data
@Builder
@Table(
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"orderId", "status"}
        )
)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;

    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private double amount;

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    private LocalDateTime createdAt;

    private int retryCount;
    private double refundedAmount;

    public Payment(Long id, Long orderId, PaymentMethod method, PaymentStatus status, double amount, LocalDateTime createdAt, int retryCount, double refundedAmount) {
        this.id = id;
        this.orderId = orderId;
        this.method = method;
        this.status = status;
        this.amount = amount;
        this.createdAt = createdAt;
        this.retryCount = retryCount;
        this.refundedAmount = refundedAmount;
    }

    public Payment() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public void setRefundedAmount(double amount) {

    }
}
