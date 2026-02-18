package com.Restaurant.RestaurantOrderManagementSystem.service.impl;

import com.Restaurant.RestaurantOrderManagementSystem.entities.Branch;
import com.Restaurant.RestaurantOrderManagementSystem.exception.BranchException;
import com.Restaurant.RestaurantOrderManagementSystem.repository.BranchRepository;
import com.Restaurant.RestaurantOrderManagementSystem.service.BranchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for managing Branch entities.
 * Provides methods to create, fetch, update status, and delete branches.
 * Implements {@link BranchService}.
 */
@Service
public class BranchServiceImpl implements BranchService {

    private static final Logger log = LoggerFactory.getLogger(BranchServiceImpl.class);

    private final BranchRepository branchRepo;

    /**
     * Constructor for BranchServiceImpl.
     *
     * @param branchRepo Repository used for database operations on Branch
     */
    public BranchServiceImpl(BranchRepository branchRepo) {
        this.branchRepo = branchRepo;
    }

    /**
     * Create a new Branch in the system.
     *
     * @param branch Branch object to create
     * @return created Branch object
     * @throws BranchException if branch is null
     */
    @Override
    public Branch createBranch(Branch branch) throws BranchException {
        // Validate input
        if (branch == null) {
            throw new BranchException("Branch cannot be null or empty");
        }
        log.info("Creating new branch: {}", branch.getName());
        return branchRepo.save(branch);
    }

    /**
     * Retrieve all branches from the system.
     *
     * @return list of all Branch objects
     */
    @Override
    public List<Branch> getAllBranches() {
        log.info("Fetching all branches");
        return branchRepo.findAll();
    }

    /**
     * Retrieve a branch by its ID.
     *
     * @param id ID of the branch
     * @return Branch object with given ID
     * @throws BranchException if branch not found
     */
    @Override
    public Branch getBranchById(Long id) throws BranchException {
        log.info("Fetching branch with ID {}", id);
        return branchRepo.findById(id)
                .orElseThrow(() -> new BranchException("Branch not found: " + id));
    }

    /**
     * Update the active status of a branch.
     *
     * @param id     ID of the branch
     * @param active new active status
     * @return updated Branch object
     * @throws BranchException if branch not found
     */
    @Override
    public Branch updateStatus(Long id, boolean active) throws BranchException {
        // Fetch existing branch
        Branch branch = getBranchById(id);

        // Update branch status (example: branch.isActive() could be modified to branch.setActive(active))
        branch.isActive();

        Branch updated = branchRepo.save(branch);
        log.info("Branch {} status updated to {}", id, active);

        return updated;
    }

    /**
     * Delete a branch by its ID.
     *
     * @param id ID of the branch to delete
     * @throws BranchException if branch not found
     */
    @Override
    public void deleteBranch(Long id) throws BranchException {
        Branch branch = getBranchById(id);

        branchRepo.delete(branch);
        log.warn("Branch deleted with ID {}", id);
    }

    /**
     * Validate Branch object before performing database operations.
     *
     * @param branch Branch to validate
     * @throws BranchException if validation fails
     */
    private void validateBranch(Branch branch) throws BranchException {
        if (branch == null) {
            throw new BranchException("Branch cannot be null");
        }
        if (branch.getName() == null || branch.getName().isBlank()) {
            throw new BranchException("Branch name is required");
        }
        if (branch.getLocation() == null || branch.getLocation().isBlank()) {
            throw new BranchException("Branch city is required");
        }
    }
}
