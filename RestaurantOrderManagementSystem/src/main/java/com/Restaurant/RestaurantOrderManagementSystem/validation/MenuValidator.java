package com.Restaurant.RestaurantOrderManagementSystem.validation;

import com.Restaurant.RestaurantOrderManagementSystem.entities.MenuItem;
import com.Restaurant.RestaurantOrderManagementSystem.exception.MenuException;
import com.Restaurant.RestaurantOrderManagementSystem.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private final BranchRepository branchRepo;

    public MenuValidator(BranchRepository branchRepo) {
        this.branchRepo = branchRepo;
    }

    public void validateMenuItem(MenuItem item) {

        if (item.getName() == null || item.getName().isBlank()) {
            throw new MenuException("Menu item name is required");
        }

        if (item.getPrice() <= 0) {
            throw new MenuException("Price must be greater than zero");
        }

        if (item.getPreparationTimeMinutes() <= 0) {
            throw new MenuException("Preparation time must be positive");
        }

        if (item.getBranch() == null || item.getBranch().getId() == null) {
            throw new MenuException("Branch is required");
        }

        branchRepo.findById(item.getBranch().getId())
                .orElseThrow(() -> new MenuException("Branch not found"));
    }
}
