package com.Restaurant.RestaurantOrderManagementSystem.contoller;

import com.Restaurant.RestaurantOrderManagementSystem.entities.Branch;
import com.Restaurant.RestaurantOrderManagementSystem.exception.BranchException;
import com.Restaurant.RestaurantOrderManagementSystem.service.impl.BranchServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing restaurant branches.
 *
 * Responsibilities:
 * - Create branch
 * - Retrieve branch details
 * - Activate/Deactivate branch
 * - Delete branch
 *
 * API Base Path: /api/branches
 *
 * Design:
 * Controller → Service → Repository → Database
 *
 * Exceptions:
 * - BranchException when branch not found or invalid state
 *
 * Author: Restaurant OMS
 */
@Tag(name = "Branch API", description = "Branch management operations")
@RestController
@RequestMapping("/api/branches")
public class BranchController {

    private static final Logger log = LoggerFactory.getLogger(BranchController.class);
    private final BranchServiceImpl branchService;

    public BranchController(BranchServiceImpl branchService) {
        this.branchService = branchService;
    }

    /**
     * Create a new restaurant branch.
     *
     * @param branch branch details
     * @return created branch
     * @throws BranchException if validation fails
     */
    @Operation(summary = "Create new branch")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Branch created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid branch data")
    })
    @PostMapping
    public Branch createBranch(@RequestBody Branch branch) throws BranchException {
        log.info("Creating branch: {}", branch.getName());
        return branchService.createBranch(branch);
    }

    /**
     * Fetch all restaurant branches.
     *
     * @return list of branches
     */
    @Operation(summary = "Get all branches")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Branches fetched successfully")
    })
    @GetMapping
    public List<Branch> getAllBranches() {
        log.info("Fetching all branches");
        return branchService.getAllBranches();
    }

    /**
     * Fetch branch by ID.
     *
     * @param id branch ID
     * @return branch details
     * @throws BranchException if branch not found
     */
    @Operation(summary = "Get branch by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Branch found"),
            @ApiResponse(responseCode = "404", description = "Branch not found")
    })
    @GetMapping("/{id}")
    public Branch getBranch(@PathVariable Long id) throws BranchException {
        log.info("Fetching branch {}", id);
        return branchService.getBranchById(id);
    }

    /**
     * Activate or deactivate a branch.
     *
     * @param id branch ID
     * @param active true = activate, false = deactivate
     * @return updated branch
     * @throws BranchException if branch not found
     */
    @Operation(summary = "Activate or deactivate branch")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Branch status updated"),
            @ApiResponse(responseCode = "404", description = "Branch not found")
    })
    @PutMapping("/{id}/status")
    public Branch updateStatus(@PathVariable Long id,
                               @RequestParam boolean active) throws BranchException {
        log.info("Updating branch {} status to {}", id, active);
        return branchService.updateStatus(id, active);
    }

    /**
     * Delete a branch.
     *
     * @param id branch ID
     * @throws BranchException if branch not found
     */
    @Operation(summary = "Delete branch")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Branch deleted"),
            @ApiResponse(responseCode = "404", description = "Branch not found")
    })
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) throws BranchException {
        log.warn("Deleting branch {}", id);
        branchService.deleteBranch(id);
    }
}
