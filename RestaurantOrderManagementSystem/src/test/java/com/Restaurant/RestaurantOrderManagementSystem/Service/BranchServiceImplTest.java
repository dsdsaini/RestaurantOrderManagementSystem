package com.Restaurant.RestaurantOrderManagementSystem.Service;

import com.Restaurant.RestaurantOrderManagementSystem.entities.Branch;
import com.Restaurant.RestaurantOrderManagementSystem.exception.BranchException;
import com.Restaurant.RestaurantOrderManagementSystem.repository.BranchRepository;
import com.Restaurant.RestaurantOrderManagementSystem.service.impl.BranchServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BranchServiceImplTest {

    @Mock
    private BranchRepository branchRepo;

    @InjectMocks
    private BranchServiceImpl branchService;

    private Branch branch;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        branch = new Branch();
        branch.setId(1L);
        branch.setName("Main Branch");
        branch.setLocation("Delhi");
        branch.setActive(true);
    }

    // -------- createBranch --------

    @Test
    void createBranch_success() throws Exception, BranchException {
        when(branchRepo.save(branch)).thenReturn(branch);

        Branch result = branchService.createBranch(branch);

        assertNotNull(result);
        assertEquals("Main Branch", result.getName());
        verify(branchRepo).save(branch);
    }

    // -------- getAllBranches --------

    @Test
    void getAllBranches_returnsList() {
        when(branchRepo.findAll()).thenReturn(List.of(branch));

        List<Branch> result = branchService.getAllBranches();

        assertEquals(1, result.size());
        verify(branchRepo).findAll();
    }

    // -------- getBranchById --------

    @Test
    void getBranchById_found() throws Exception, BranchException {
        when(branchRepo.findById(1L)).thenReturn(Optional.of(branch));

        Branch result = branchService.getBranchById(1L);

        assertEquals(1L, result.getId());
    }
    // -------- updateStatus --------

    @Test
    void updateStatus_success() throws Exception, BranchException {
        when(branchRepo.findById(1L)).thenReturn(Optional.of(branch));
        when(branchRepo.save(branch)).thenReturn(branch);

        Branch result = branchService.updateStatus(1L, true);

        assertNotNull(result);
        verify(branchRepo).save(branch);
    }

    @Test
    void updateStatus_branchNotFound() {
        when(branchRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BranchException.class,
                () -> branchService.updateStatus(1L, true));
    }

    // -------- deleteBranch --------

    @Test
    void deleteBranch_success() throws Exception, BranchException {
        when(branchRepo.findById(1L)).thenReturn(Optional.of(branch));
        doNothing().when(branchRepo).delete(branch);

        branchService.deleteBranch(1L);

        verify(branchRepo).delete(branch);
    }

    @Test
    void deleteBranch_notFound() {
        when(branchRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BranchException.class,
                () -> branchService.deleteBranch(1L));

        verify(branchRepo, never()).delete(any());
    }
}
