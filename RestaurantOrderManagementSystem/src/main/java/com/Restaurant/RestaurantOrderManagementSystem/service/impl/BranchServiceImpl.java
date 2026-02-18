package com.Restaurant.RestaurantOrderManagementSystem.service.impl;

import com.Restaurant.RestaurantOrderManagementSystem.entities.Branch;
import com.Restaurant.RestaurantOrderManagementSystem.exception.BranchException;
import com.Restaurant.RestaurantOrderManagementSystem.repository.BranchRepository;
import com.Restaurant.RestaurantOrderManagementSystem.service.BranchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BranchServiceImpl implements BranchService {

    private static final Logger log = LoggerFactory.getLogger(BranchServiceImpl.class);

    private final BranchRepository branchRepo;

    public BranchServiceImpl(BranchRepository branchRepo) {
        this.branchRepo = branchRepo;
    }

    @Override
    public Branch createBranch(Branch branch) throws BranchException {
        if (branch == null) {
            throw new BranchException("Branch cannot be null or empty");
        }
        return branchRepo.save(branch);
    }

    @Override
    public List<Branch> getAllBranches() {

        log.info("Fetching all branches");

        return branchRepo.findAll();
    }

    @Override
    public Branch getBranchById(Long id) throws BranchException {

        return branchRepo.findById(id)
                .orElseThrow(() -> new BranchException("Branch not found: " + id));
    }

    @Override
    public Branch updateStatus(Long id, boolean active) throws BranchException {

        Branch branch = getBranchById(id);

        branch.isActive();

        Branch updated = branchRepo.save(branch);

        log.info("Branch {} status updated to {}", id, active);

        return updated;
    }


    @Override
    public void deleteBranch(Long id) throws BranchException {

        Branch branch = getBranchById(id);

        branchRepo.delete(branch);

        log.warn("Branch deleted {}", id);
    }


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
