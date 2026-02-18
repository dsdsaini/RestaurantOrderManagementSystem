package com.Restaurant.RestaurantOrderManagementSystem.contoller;

import com.Restaurant.RestaurantOrderManagementSystem.entities.MenuItem;
import com.Restaurant.RestaurantOrderManagementSystem.enums.Category;
import com.Restaurant.RestaurantOrderManagementSystem.enums.DietType;
import com.Restaurant.RestaurantOrderManagementSystem.enums.MenuType;
import com.Restaurant.RestaurantOrderManagementSystem.service.impl.MenuServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing Menu items.
 * Supports CRUD operations and filtering by branch, type, diet, and category.
 */
@RestController
@RequestMapping("/api/menus")
@Tag(name = "Menu Controller", description = "Endpoints for managing restaurant menu items")
public class MenuController {

    private final MenuServiceImpl menuService;

    public MenuController(MenuServiceImpl menuService) {
        this.menuService = menuService;
    }

    /**
     * Add a new menu item to the system.
     *
     * @param item MenuItem object containing item details
     * @return Saved MenuItem with generated ID
     */
    @PostMapping
    @Operation(summary = "Add Menu Item", description = "Adds a new menu item to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Menu item added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid menu item data")
    })
    public MenuItem addItem(@RequestBody MenuItem item) {
        return menuService.addItem(item);
    }

    /**
     * Get all menu items for a given branch.
     *
     * @param branchId Branch ID
     * @return List of available menu items
     */
    @GetMapping("/branch/{branchId}")
    @Operation(summary = "Get Menu by Branch", description = "Returns all available menu items for a branch")
    public List<MenuItem> getByBranch(@PathVariable Long branchId) {
        return menuService.getMenuByBranch(branchId);
    }

    /**
     * Filter menu items by type, diet type, and category.
     *
     * @param branchId Branch ID
     * @param type Menu type (BREAKFAST, LUNCH, DINNER, etc.)
     * @param dietType Optional diet type (VEG, NON_VEG, VEGAN, etc.)
     * @param category Optional category (STARTER, MAIN, DESSERT, etc.)
     * @return List of menu items matching the filters
     */
    @GetMapping("/filter")
    @Operation(summary = "Filter Menu Items", description = "Filters menu items by type, diet type, and category")
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

    /**
     * Bulk update multiple menu items at once.
     *
     * @param items List of MenuItem objects to update
     * @return List of updated MenuItem objects
     */
    @PostMapping("/bulk")
    @Operation(summary = "Bulk Update Menu Items", description = "Update multiple menu items at once")
    public List<MenuItem> bulkUpdate(@RequestBody List<MenuItem> items) {
        return menuService.bulkUpdate(items);
    }
}
