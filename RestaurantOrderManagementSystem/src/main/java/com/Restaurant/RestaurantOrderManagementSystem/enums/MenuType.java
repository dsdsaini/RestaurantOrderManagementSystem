package com.Restaurant.RestaurantOrderManagementSystem.enums;

import java.time.LocalTime;

public enum MenuType {
    BREAKFAST(LocalTime.of(6,0), LocalTime.of(11,0)),
    LUNCH(LocalTime.of(11,0), LocalTime.of(16,0)),
    DINNER(LocalTime.of(16,0), LocalTime.of(22,0));

    private final LocalTime start;
    private final LocalTime end;

    MenuType(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
    }


    public boolean isActive() {
        LocalTime now = LocalTime.now();
        return !now.isBefore(start) && !now.isAfter(end);
    }
}
