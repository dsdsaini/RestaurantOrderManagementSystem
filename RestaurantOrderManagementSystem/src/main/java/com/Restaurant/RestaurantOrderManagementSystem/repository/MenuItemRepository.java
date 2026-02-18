package com.Restaurant.RestaurantOrderManagementSystem.repository;

import com.Restaurant.RestaurantOrderManagementSystem.entities.MenuItem;
import com.Restaurant.RestaurantOrderManagementSystem.enums.MenuType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    List<MenuItem> findByBranchId(Long branchId);

    List<MenuItem> findByBranchIdAndMenuType(Long branchId, MenuType type);

}
