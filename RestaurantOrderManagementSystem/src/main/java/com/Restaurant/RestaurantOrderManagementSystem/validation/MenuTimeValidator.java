package com.Restaurant.RestaurantOrderManagementSystem.validation;

import com.Restaurant.RestaurantOrderManagementSystem.enums.MenuType;
import java.time.LocalTime;

/**
 * Utility class to validate whether a particular menu type
 * is available based on the current time of the day.
 *
 * <p>Menus are available during specific time windows:
 * <ul>
 *     <li>BREAKFAST: 6:00 AM - 11:00 AM</li>
 *     <li>LUNCH: 11:00 AM - 4:00 PM</li>
 *     <li>DINNER: 4:00 PM - 10:00 PM</li>
 * </ul>
 * </p>
 */
public class MenuTimeValidator {

    /**
     * Checks if the given menu type is available at the current local time.
     *
     * @param type the MenuType to validate (BREAKFAST, LUNCH, DINNER)
     * @return true if the menu is currently available, false otherwise
     */
    public static boolean isMenuAvailable(MenuType type) {
        // Get the current local time
        LocalTime now = LocalTime.now();

        // Check availability based on menu type and predefined time windows
        return switch (type) {
            case BREAKFAST -> now.isAfter(LocalTime.of(6, 0)) && now.isBefore(LocalTime.of(11, 0));
            case LUNCH -> now.isAfter(LocalTime.of(11, 0)) && now.isBefore(LocalTime.of(16, 0));
            case DINNER -> now.isAfter(LocalTime.of(16, 0)) && now.isBefore(LocalTime.of(22, 0));
        };
    }
}
