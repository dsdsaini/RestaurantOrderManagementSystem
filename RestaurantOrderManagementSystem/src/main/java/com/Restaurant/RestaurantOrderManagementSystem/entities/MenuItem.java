package com.Restaurant.RestaurantOrderManagementSystem.entities;

import com.Restaurant.RestaurantOrderManagementSystem.enums.Category;
import com.Restaurant.RestaurantOrderManagementSystem.enums.DietType;
import com.Restaurant.RestaurantOrderManagementSystem.enums.MenuType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private double price;
    private int preparationTimeMinutes;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private DietType dietType;

    @Enumerated(EnumType.STRING)
    private MenuType menuType;

    private boolean available = true;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    public MenuItem(Long id, String name, String description, double price, int preparationTimeMinutes, Category category, DietType dietType, MenuType menuType, boolean available, Branch branch) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.preparationTimeMinutes = preparationTimeMinutes;
        this.category = category;
        this.dietType = dietType;
        this.menuType = menuType;
        this.available = available;
        this.branch = branch;
    }

    public MenuItem() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getPreparationTimeMinutes() {
        return preparationTimeMinutes;
    }

    public void setPreparationTimeMinutes(int preparationTimeMinutes) {
        this.preparationTimeMinutes = preparationTimeMinutes;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public DietType getDietType() {
        return dietType;
    }

    public void setDietType(DietType dietType) {
        this.dietType = dietType;
    }

    public MenuType getMenuType() {
        return menuType;
    }

    public void setMenuType(MenuType menuType) {
        this.menuType = menuType;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }
}