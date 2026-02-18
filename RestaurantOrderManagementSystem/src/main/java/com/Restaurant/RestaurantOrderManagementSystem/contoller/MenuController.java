package com.Restaurant.RestaurantOrderManagementSystem.contoller;

import com.Restaurant.RestaurantOrderManagementSystem.entities.MenuItem;
import com.Restaurant.RestaurantOrderManagementSystem.enums.Category;
import com.Restaurant.RestaurantOrderManagementSystem.enums.DietType;
import com.Restaurant.RestaurantOrderManagementSystem.enums.MenuType;
import com.Restaurant.RestaurantOrderManagementSystem.service.impl.MenuServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
public class MenuController {
    private final MenuServiceImpl menuService;

    public MenuController(MenuServiceImpl menuService) {
        this.menuService = menuService;
    }

    @PostMapping
    public MenuItem addItem(@RequestBody MenuItem item) {
        return menuService.addItem(item);
    }

    @GetMapping("/branch/{branchId}")
    public List<MenuItem> getByBranch(@PathVariable Long branchId) {
        return menuService.getMenuByBranch(branchId);
    }

    @GetMapping("/filter")
    public List<MenuItem> filter(
            @RequestParam Long branchId,
            @RequestParam MenuType type,
            @RequestParam(required = false) DietType dietType,
            @RequestParam(required = false) Category category
    ) {
        if (dietType != null && category != null) {
            return menuService.getMenuByTypeAndDietTypeAndCategory(branchId, type, dietType, category);
        }
        if (dietType != null) {
            return menuService.getMenuByTypeAndDietType(branchId, type, dietType);
        }
        if (category != null) {
            return menuService.getMenuByTypeAndCategory(branchId, type, category);
        }
        return menuService.getMenuByType(branchId, type);
    }

    @PostMapping("/bulk")
    public List<MenuItem> bulkUpdate(@RequestBody List<MenuItem> items) {
        return menuService.bulkUpdate(items);
    }
}

