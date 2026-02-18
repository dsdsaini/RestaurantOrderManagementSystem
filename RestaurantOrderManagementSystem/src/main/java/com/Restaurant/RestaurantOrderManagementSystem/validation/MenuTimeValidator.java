package com.Restaurant.RestaurantOrderManagementSystem.validation;

import com.Restaurant.RestaurantOrderManagementSystem.enums.MenuType;

import java.time.LocalTime;

public class MenuTimeValidator {

    public static boolean isMenuAvailable(MenuType type) {
        LocalTime now = LocalTime.now();

        return switch (type) {
            case BREAKFAST -> now.isAfter(LocalTime.of(6, 0)) && now.isBefore(LocalTime.of(11, 0));
            case LUNCH -> now.isAfter(LocalTime.of(11, 0)) && now.isBefore(LocalTime.of(16, 0));
            case DINNER -> now.isAfter(LocalTime.of(16, 0)) && now.isBefore(LocalTime.of(22, 0));
        };
    }
}