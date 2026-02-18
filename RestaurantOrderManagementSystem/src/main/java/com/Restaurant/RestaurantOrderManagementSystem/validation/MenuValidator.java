package com.Restaurant.RestaurantOrderManagementSystem.validation;

import com.Restaurant.RestaurantOrderManagementSystem.entities.MenuItem;
import com.Restaurant.RestaurantOrderManagementSystem.exception.MenuException;
import com.Restaurant.RestaurantOrderManagementSystem.repository.BranchRepository;
import org.springframework.stereotype.Component;

/**
 * Validator class for MenuItem entities.
 * <p>
 * This class performs validation checks to ensure that a MenuItem
 * is correctly defined before it is persisted or updated.
 * </p>
 */
@Component
public class MenuValidator {

    private final BranchRepository branchRepo;

    /**
     * Constructs a MenuValidator with the required BranchRepository.
     *
     * @param branchRepo the BranchRepository to validate branch existence
     */
    public MenuValidator(BranchRepository branchRepo) {
        this.branchRepo = branchRepo;
    }

    /**
     * Validates the given MenuItem object.
     * <p>
     * The validation checks include:
     * <ul>
     *     <li>Name is not null or blank</li>
     *     <li>Price is greater than zero</li>
     *     <li>Preparation time is positive</li>
     *     <li>Branch is specified and exists in the repository</li>
     * </ul>
     * </p>
     *
     * @param item the MenuItem to validate
     * @throws MenuException if any validation fails
     */
    public void validateMenuItem(MenuItem item) {

        // Validate menu item name
        if (item.getName() == null || item.getName().isBlank()) {
            throw new MenuException("Menu item name is required");
        }

        // Validate price
        if (item.getPrice() <= 0) {
            throw new MenuException("Price must be greater than zero");
        }

        // Validate preparation time
        if (item.getPreparationTimeMinutes() <= 0) {
            throw new MenuException("Preparation time must be positive");
        }

        // Validate branch association
        if (item.getBranch() == null || item.getBranch().getId() == null) {
            throw new MenuException("Branch is required");
        }

        // Check if the branch actually exists in the repository
        branchRepo.findById(item.getBranch().getId())
                .orElseThrow(() -> new MenuException("Branch not found"));
    }
}
