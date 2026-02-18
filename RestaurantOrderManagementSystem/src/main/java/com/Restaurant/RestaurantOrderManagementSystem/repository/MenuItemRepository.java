package com.Restaurant.RestaurantOrderManagementSystem.repository;

import com.Restaurant.RestaurantOrderManagementSystem.entities.MenuItem;
import com.Restaurant.RestaurantOrderManagementSystem.enums.MenuType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for managing MenuItem entities.
 * <p>
 * Provides standard CRUD operations via JpaRepository and
 * custom methods to fetch menu items by branch and menu type.
 * </p>
 */
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    /**
     * Finds all menu items for a given branch.
     *
     * @param branchId the ID of the branch
     * @return a list of MenuItem objects for the branch
     */
    List<MenuItem> findByBranchId(Long branchId);

    /**
     * Finds all menu items for a given branch and menu type.
     * <p>
     * Useful for filtering menus based on type, such as BREAKFAST, LUNCH, or DINNER.
     * </p>
     *
     * @param branchId the ID of the branch
     * @param type     the type of menu (MenuType enum)
     * @return a list of MenuItem objects for the branch with the specified type
     */
    List<MenuItem> findByBranchIdAndMenuType(Long branchId, MenuType type);
}

