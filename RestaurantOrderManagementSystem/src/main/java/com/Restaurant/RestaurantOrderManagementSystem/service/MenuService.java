package com.Restaurant.RestaurantOrderManagementSystem.service;

import com.Restaurant.RestaurantOrderManagementSystem.enums.Category;
import com.Restaurant.RestaurantOrderManagementSystem.enums.DietType;
import com.Restaurant.RestaurantOrderManagementSystem.enums.MenuType;
import com.Restaurant.RestaurantOrderManagementSystem.entities.MenuItem;


import java.util.List;

public interface MenuService {

    MenuItem addItem(MenuItem item);

    List<MenuItem> getMenuByBranch(Long branchId);

    List<MenuItem> bulkUpdate(List<MenuItem> items);

    List<MenuItem> getMenuByType(Long branchId, MenuType type);

    List<MenuItem> getMenuByTypeAndDietType(Long branchId, MenuType type, DietType dietType);

    List<MenuItem> getMenuByTypeAndCategory(Long branchId, MenuType type, Category category);

    List<MenuItem> getMenuByTypeAndDietTypeAndCategory(Long branchId, MenuType type, DietType dietType, Category category);
}
