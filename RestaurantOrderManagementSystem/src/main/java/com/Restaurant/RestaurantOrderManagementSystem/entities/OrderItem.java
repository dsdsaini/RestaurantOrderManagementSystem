package com.Restaurant.RestaurantOrderManagementSystem.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Order order;

    @ManyToOne
    private MenuItem menuItem;

    private int quantity;

    private boolean cancelled;

    @Column(length = 500)
    private String specialInstructions;

    public OrderItem() {

    }

    public double getTotalPrice() {
        return menuItem.getPrice() * quantity;
    }

    public OrderItem(Long id, Order order, MenuItem menuItem, int quantity, boolean cancelled, String specialInstructions) {
        this.id = id;
        this.order = order;
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.cancelled = cancelled;
        this.specialInstructions = specialInstructions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }
}
