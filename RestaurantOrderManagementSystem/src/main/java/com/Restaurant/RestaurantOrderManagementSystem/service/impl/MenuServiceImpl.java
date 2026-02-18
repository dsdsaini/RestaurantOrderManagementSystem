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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.Restaurant.RestaurantOrderManagementSystem.repository.MenuItemRepository;
import com.Restaurant.RestaurantOrderManagementSystem.service.MenuService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {

    private static final Logger log = LoggerFactory.getLogger(MenuServiceImpl.class);

    private final MenuItemRepository menuRepo;
    private final ComboMealRepository comboRepo;
    private final MenuValidator menuValidator;

    public MenuServiceImpl(MenuItemRepository menuRepo, ComboMealRepository comboRepo, MenuValidator menuValidator) {
        this.menuRepo = menuRepo;
        this.comboRepo = comboRepo;
        this.menuValidator = menuValidator;
    }

    @Override
    @Transactional
    public MenuItem addItem(MenuItem item) {
        // Use dedicated validator
        menuValidator.validateMenuItem(item);

        if (!MenuTimeValidator.isMenuAvailable(item.getMenuType())) {
            throw new BusinessException("Menu not available at this time");
        }

        log.info("Adding menu item {} in branch {}", item.getName(), item.getBranch().getId());
        return menuRepo.save(item);
    }

    @Override
    public List<MenuItem> getMenuByBranch(Long branchId) {
        return menuRepo.findByBranchId(branchId)
                .stream()
                .filter(MenuItem::isAvailable)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<MenuItem> bulkUpdate(List<MenuItem> items) {
        items.forEach(menuValidator::validateMenuItem);
        log.info("Bulk updating {} menu items", items.size());
        return menuRepo.saveAll(items);
    }

    @Override
    public List<MenuItem> getMenuByType(Long branchId, MenuType type) {
        return menuRepo.findByBranchIdAndMenuType(branchId, type)
                .stream()
                .filter(MenuItem::isAvailable)
                .collect(Collectors.toList());
    }

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

