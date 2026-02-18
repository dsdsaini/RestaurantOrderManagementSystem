package com.Restaurant.RestaurantOrderManagementSystem.contoller;

import com.Restaurant.RestaurantOrderManagementSystem.entities.Branch;
import com.Restaurant.RestaurantOrderManagementSystem.exception.BranchException;
import com.Restaurant.RestaurantOrderManagementSystem.service.impl.BranchServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/branches")
public class BranchController {

    private static final Logger log = LoggerFactory.getLogger(BranchController.class);
    private final BranchServiceImpl branchService;

    public BranchController(BranchServiceImpl branchService) {
        this.branchService = branchService;
    }


    /**
     * Create new branch
     */
    @PostMapping
    public Branch createBranch(@RequestBody Branch branch) throws BranchException {
        log.info("Creating branch: {}", branch.getName());
        return branchService.createBranch(branch);
    }

    /**
     * Get all branches
     */
    @GetMapping
    public List<Branch> getAllBranches() {
        log.info("Fetching all branches");
        return branchService.getAllBranches();
    }

    /**
     * Get branch by id
     */
    @GetMapping("/{id}")
    public Branch getBranch(@PathVariable Long id) throws BranchException {
        log.info("Fetching branch {}", id);
        return branchService.getBranchById(id);
    }

    /**
     * Activate/Deactivate branch
     */
    @PutMapping("/{id}/status")
    public Branch updateStatus(@PathVariable Long id,
                               @RequestParam boolean active) throws BranchException {
        log.info("Updating branch {} status to {}", id, active);
        return branchService.updateStatus(id, active);
    }

    /**
     * Delete branch
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) throws BranchException {
        log.warn("Deleting branch {}", id);
        branchService.deleteBranch(id);
    }
}

