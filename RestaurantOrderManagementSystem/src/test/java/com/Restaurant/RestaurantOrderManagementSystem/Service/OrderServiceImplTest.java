package com.Restaurant.RestaurantOrderManagementSystem.Service;

import com.Restaurant.RestaurantOrderManagementSystem.entities.*;
import com.Restaurant.RestaurantOrderManagementSystem.enums.OrderStatus;
import com.Restaurant.RestaurantOrderManagementSystem.exception.BusinessException;
import com.Restaurant.RestaurantOrderManagementSystem.repository.BranchRepository;
import com.Restaurant.RestaurantOrderManagementSystem.repository.MenuItemRepository;
import com.Restaurant.RestaurantOrderManagementSystem.repository.OrderRepository;
import com.Restaurant.RestaurantOrderManagementSystem.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepo;

    @Mock
    private MenuItemRepository menuRepo;

    @Mock
    private BranchRepository branchRepo;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Branch branch;
    private MenuItem menuItem;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        branch = new Branch();
        branch.setId(1L);
        branch.setActive(true);

        menuItem = new MenuItem();
        menuItem.setId(10L);
        menuItem.setName("Pizza");
        menuItem.setPrice(200);
        menuItem.setAvailable(true);
    }


    @Test
    void createOrder_success() {
        Map<Long, Integer> items = Map.of(10L, 2);
        Map<Long, String> instructions = Map.of(10L, "Extra cheese");

        when(branchRepo.findById(1L)).thenReturn(Optional.of(branch));
        when(menuRepo.findById(10L)).thenReturn(Optional.of(menuItem));
        when(orderRepo.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        Order order = orderService.createOrder(1L, "Ravi", items, instructions, 20);

        assertNotNull(order);
        assertEquals("Ravi", order.getCustomerName());
        assertEquals(OrderStatus.CREATED, order.getStatus());
        assertEquals(1, order.getItems().size());

        // subtotal = 200 * 2 = 400
        // tax = 72
        // total = 492
        assertEquals(492, order.getTotalAmount(), 0.01);

        verify(orderRepo).save(any(Order.class));
    }

    @Test
    void createOrder_branchNotFound() {
        when(branchRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class,
                () -> orderService.createOrder(1L, "Ravi", Map.of(), Map.of(), 0));
    }

    @Test
    void createOrder_branchInactive() {
        branch.setActive(false);
        when(branchRepo.findById(1L)).thenReturn(Optional.of(branch));

        assertThrows(BusinessException.class,
                () -> orderService.createOrder(1L, "Ravi", Map.of(), Map.of(), 0));
    }

    @Test
    void createOrder_menuItemNotFound() {
        when(branchRepo.findById(1L)).thenReturn(Optional.of(branch));
        when(menuRepo.findById(10L)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class,
                () -> orderService.createOrder(1L, "Ravi", Map.of(10L, 1), Map.of(), 0));
    }

    @Test
    void createOrder_menuItemUnavailable() {
        menuItem.setAvailable(false);

        when(branchRepo.findById(1L)).thenReturn(Optional.of(branch));
        when(menuRepo.findById(10L)).thenReturn(Optional.of(menuItem));

        assertThrows(BusinessException.class,
                () -> orderService.createOrder(1L, "Ravi", Map.of(10L, 1), Map.of(), 0));
    }


    @Test
    void updateStatus_success() {
        Order order = new Order();
        order.setId(5L);

        when(orderRepo.findById(5L)).thenReturn(Optional.of(order));
        when(orderRepo.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        Order updated = orderService.updateStatus(5L, "DELIVERED");

        assertEquals(OrderStatus.DELIVERED, updated.getStatus());
        verify(orderRepo).save(order);
    }

    @Test
    void updateStatus_invalidStatus() {
        Order order = new Order();
        when(orderRepo.findById(5L)).thenReturn(Optional.of(order));

        assertThrows(BusinessException.class,
                () -> orderService.updateStatus(5L, "WRONG_STATUS"));
    }

    @Test
    void updateStatus_orderNotFound() {
        when(orderRepo.findById(5L)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class,
                () -> orderService.updateStatus(5L, "CREATED"));
    }


    @Test
    void updateOrderStatus_success() {
        Order order = new Order();
        order.setId(7L);

        when(orderRepo.findById(7L)).thenReturn(Optional.of(order));

        orderService.updateOrderStatus(7L, OrderStatus.CANCELLED);

        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        verify(orderRepo).save(order);
    }

    @Test
    void updateOrderStatus_orderNotFound() {
        when(orderRepo.findById(7L)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class,
                () -> orderService.updateOrderStatus(7L, OrderStatus.CREATED));
    }
}
