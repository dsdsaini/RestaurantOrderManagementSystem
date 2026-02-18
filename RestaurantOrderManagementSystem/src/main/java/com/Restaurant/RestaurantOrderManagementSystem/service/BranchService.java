package com.Restaurant.RestaurantOrderManagementSystem.service;

import com.Restaurant.RestaurantOrderManagementSystem.entities.Branch;
import com.Restaurant.RestaurantOrderManagementSystem.exception.BranchException;

import java.util.List;

public interface BranchService {

    Branch createBranch(Branch branch) throws BranchException;

    List<Branch> getAllBranches();

    Branch getBranchById(Long id) throws BranchException;

    Branch updateStatus(Long id, boolean active) throws BranchException;

    void deleteBranch(Long id) throws BranchException;
}

