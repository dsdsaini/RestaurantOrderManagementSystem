package com.Restaurant.RestaurantOrderManagementSystem.service.impl;

import com.Restaurant.RestaurantOrderManagementSystem.enums.Category;
import com.Restaurant.RestaurantOrderManagementSystem.enums.DietType;
import com.Restaurant.RestaurantOrderManagementSystem.enums.MenuType;
import com.Restaurant.RestaurantOrderManagementSystem.entities.MenuItem;
import com.Restaurant.RestaurantOrderManagementSystem.exception.BusinessException;
import com.Restaurant.RestaurantOrderManagementSystem.exception.MenuException;
import com.Restaurant.RestaurantOrderManagementSystem.repository.ComboMealRepository;
import com.Restaurant.RestaurantOrderManagementSystem.validation.MenuTimeValidator;
import com.Restaurant.RestaurantOrderManagementSystem.validation.MenuValidator;
import com.Restaurant.RestaurantOrderManagementSystem.repository.MenuItemRepository;
import com.Restaurant.RestaurantOrderManagementSystem.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link MenuService} for managing restaurant menu items.
 * <p>
 * Provides CRUD operations and filtering based on branch, menu type, diet type, and category.
 * Ensures that items are validated before saving and only available items are returned.
 * </p>
 */
@Service
public class MenuServiceImpl implements MenuService {

    private static final Logger log = LoggerFactory.getLogger(MenuServiceImpl.class);

    private final MenuItemRepository menuRepo;
    private final ComboMealRepository comboRepo;
    private final MenuValidator menuValidator;

    /**
     * Constructor to initialize repositories and validators.
     *
     * @param menuRepo      Repository for MenuItem entities
     * @param comboRepo     Repository for combo meals (not currently used in logic)
     * @param menuValidator Validator for menu items
     */
    public MenuServiceImpl(MenuItemRepository menuRepo, ComboMealRepository comboRepo, MenuValidator menuValidator) {
        this.menuRepo = menuRepo;
        this.comboRepo = comboRepo;
        this.menuValidator = menuValidator;
    }

    /**
     * Adds a new menu item after validation and time check.
     *
     * @param item MenuItem to add
     * @return saved MenuItem
     * @throws BusinessException if menu item is not available at this time
     */
    @Override
    @Transactional
    public MenuItem addItem(MenuItem item) {
        // Validate menu item fields
        menuValidator.validateMenuItem(item);

        // Check menu availability based on menu type (e.g., breakfast, lunch)
        if (!MenuTimeValidator.isMenuAvailable(item.getMenuType())) {
            throw new BusinessException("Menu not available at this time");
        }

        log.info("Adding menu item {} in branch {}", item.getName(), item.getBranch().getId());
        return menuRepo.save(item);
    }

    /**
     * Get all available menu items for a branch.
     *
     * @param branchId Branch ID
     * @return List of available MenuItem objects
     */
    @Override
    public List<MenuItem> getMenuByBranch(Long branchId) {
        return menuRepo.findByBranchId(branchId)
                .stream()
                .filter(MenuItem::isAvailable) // Only return available items
                .collect(Collectors.toList());
    }

    /**
     * Bulk update multiple menu items.
     *
     * @param items List of MenuItem objects to update
     * @return List of saved MenuItem objects
     */
    @Override
    @Transactional
    public List<MenuItem> bulkUpdate(List<MenuItem> items) {
        items.forEach(menuValidator::validateMenuItem); // Validate each item
        log.info("Bulk updating {} menu items", items.size());
        return menuRepo.saveAll(items);
    }

    /**
     * Get available menu items for a branch filtered by menu type.
     *
     * @param branchId Branch ID
     * @param type     MenuType (e.g., BREAKFAST, LUNCH)
     * @return List of MenuItem objects
     */
    @Override
    public List<MenuItem> getMenuByType(Long branchId, MenuType type) {
        return menuRepo.findByBranchIdAndMenuType(branchId, type)
                .stream()
                .filter(MenuItem::isAvailable)
                .collect(Collectors.toList());
    }

    /**
     * Get menu items filtered by type and diet type.
     *
     * @param branchId Branch ID
     * @param type     MenuType
     * @param dietType DietType (e.g., VEGAN, VEGETARIAN)
     * @return List of filtered MenuItem objects
     * @throws MenuException if diet type is invalid
     */
    @Override
    public List<MenuItem> getMenuByTypeAndDietType(Long branchId, MenuType type, DietType dietType) {
        DietType dt;
        try {
            dt = DietType.valueOf(dietType.toString().toUpperCase());
        } catch (Exception e) {
            throw new MenuException("Invalid diet type: " + dietType);
        }

        return getMenuByType(branchId, type).stream()
                .filter(m -> m.getDietType() == dt)
                .collect(Collectors.toList());
    }

    /**
     * Get menu items filtered by type and category.
     *
     * @param branchId Branch ID
     * @param type     MenuType
     * @param category Category (e.g., STARTER, MAIN_COURSE)
     * @return List of filtered MenuItem objects
     * @throws MenuException if category is invalid
     */
    @Override
    public List<MenuItem> getMenuByTypeAndCategory(Long branchId, MenuType type, Category category) {
        Category cat;
        try {
            cat = Category.valueOf(category.toString().toUpperCase());
        } catch (Exception e) {
            throw new MenuException("Invalid category: " + category);
        }

        return getMenuByType(branchId, type).stream()
                .filter(m -> m.getCategory() == cat)
                .collect(Collectors.toList());
    }

    /**
     * Get menu items filtered by type, diet type, and category.
     *
     * @param branchId Branch ID
     * @param type     MenuType
     * @param dietType DietType
     * @param category Category
     * @return List of filtered MenuItem objects
     * @throws MenuException if diet type or category is invalid
     */
    @Override
    public List<MenuItem> getMenuByTypeAndDietTypeAndCategory(Long branchId, MenuType type, DietType dietType, Category category) {
        DietType dt;
        Category cat;
        try {
            dt = DietType.valueOf(dietType.toString().toUpperCase());
            cat = Category.valueOf(category.toString().toUpperCase());
        } catch (Exception e) {
            throw new MenuException("Invalid diet type or category");
        }

        return getMenuByType(branchId, type).stream()
                .filter(m -> m.getDietType() == dt && m.getCategory() == cat)
                .collect(Collectors.toList());
    }
}
