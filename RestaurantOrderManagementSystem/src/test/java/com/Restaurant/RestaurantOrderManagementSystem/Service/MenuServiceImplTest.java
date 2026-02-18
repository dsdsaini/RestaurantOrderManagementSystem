package com.Restaurant.RestaurantOrderManagementSystem.Service;

import com.Restaurant.RestaurantOrderManagementSystem.entities.Branch;
import com.Restaurant.RestaurantOrderManagementSystem.entities.MenuItem;
import com.Restaurant.RestaurantOrderManagementSystem.enums.Category;
import com.Restaurant.RestaurantOrderManagementSystem.enums.DietType;
import com.Restaurant.RestaurantOrderManagementSystem.enums.MenuType;
import com.Restaurant.RestaurantOrderManagementSystem.exception.BusinessException;
import com.Restaurant.RestaurantOrderManagementSystem.exception.MenuException;
import com.Restaurant.RestaurantOrderManagementSystem.repository.ComboMealRepository;
import com.Restaurant.RestaurantOrderManagementSystem.repository.MenuItemRepository;
import com.Restaurant.RestaurantOrderManagementSystem.service.impl.MenuServiceImpl;
import com.Restaurant.RestaurantOrderManagementSystem.validation.MenuTimeValidator;
import com.Restaurant.RestaurantOrderManagementSystem.validation.MenuValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class MenuServiceImplTest {

    @Mock
    private MenuItemRepository menuRepo;

    @Mock
    private ComboMealRepository comboRepo;

    @Mock
    private MenuValidator menuValidator;

    @InjectMocks
    private MenuServiceImpl menuService;

    private MenuItem item;

    @BeforeEach
    void setup() {
        Branch branch = new Branch();
        branch.setId(1L);

        item = new MenuItem();
        item.setId(10L);
        item.setName("Paneer Tikka");
        item.setBranch(branch);
        item.setMenuType(MenuType.DINNER);
        item.setDietType(DietType.VEG);
        item.setCategory(Category.MAIN_COURSE);
        item.setAvailable(true);
    }

    // ---------- addItem ----------

    @Test
    void addItem_success() {
        try (MockedStatic<MenuTimeValidator> mocked = mockStatic(MenuTimeValidator.class)) {
            mocked.when(() -> MenuTimeValidator.isMenuAvailable(any())).thenReturn(true);
            when(menuRepo.save(item)).thenReturn(item);

            MenuItem saved = menuService.addItem(item);

            verify(menuValidator).validateMenuItem(item);
            verify(menuRepo).save(item);
            assertEquals(item, saved);
        }
    }

    @Test
    void addItem_menuNotAvailable() {
        try (MockedStatic<MenuTimeValidator> mocked = mockStatic(MenuTimeValidator.class)) {
            mocked.when(() -> MenuTimeValidator.isMenuAvailable(any())).thenReturn(false);

            assertThrows(BusinessException.class, () -> menuService.addItem(item));

            verify(menuValidator).validateMenuItem(item);
            verify(menuRepo, never()).save(any());
        }
    }

    // ---------- getMenuByBranch ----------

    @Test
    void getMenuByBranch_filtersAvailableOnly() {
        MenuItem unavailable = new MenuItem();
        unavailable.setAvailable(false);

        when(menuRepo.findByBranchId(1L)).thenReturn(List.of(item, unavailable));

        List<MenuItem> result = menuService.getMenuByBranch(1L);

        assertEquals(1, result.size());
        assertTrue(result.get(0).isAvailable());
    }

    // ---------- bulkUpdate ----------

    @Test
    void bulkUpdate_success() {
        List<MenuItem> items = List.of(item);
        when(menuRepo.saveAll(items)).thenReturn(items);

        List<MenuItem> result = menuService.bulkUpdate(items);

        verify(menuValidator).validateMenuItem(item);
        verify(menuRepo).saveAll(items);
        assertEquals(1, result.size());
    }

    // ---------- getMenuByType ----------

    @Test
    void getMenuByType_filtersAvailable() {
        MenuItem unavailable = new MenuItem();
        unavailable.setAvailable(false);

        when(menuRepo.findByBranchIdAndMenuType(1L, MenuType.DINNER))
                .thenReturn(List.of(item, unavailable));

        List<MenuItem> result = menuService.getMenuByType(1L, MenuType.DINNER);

        assertEquals(1, result.size());
        assertTrue(result.get(0).isAvailable());
    }

    // ---------- getMenuByTypeAndDietType ----------

    @Test
    void getMenuByTypeAndDietType_success() {
        when(menuRepo.findByBranchIdAndMenuType(1L, MenuType.DINNER))
                .thenReturn(List.of(item));

        List<MenuItem> result =
                menuService.getMenuByTypeAndDietType(1L, MenuType.DINNER, DietType.VEG);

        assertEquals(1, result.size());
    }

    @Test
    void getMenuByTypeAndDietType_invalidDietType() {
        assertThrows(MenuException.class,
                () -> menuService.getMenuByTypeAndDietType(1L, MenuType.DINNER, null));
    }

    // ---------- getMenuByTypeAndCategory ----------

    @Test
    void getMenuByTypeAndCategory_success() {
        when(menuRepo.findByBranchIdAndMenuType(1L, MenuType.DINNER))
                .thenReturn(List.of(item));

        List<MenuItem> result =
                menuService.getMenuByTypeAndCategory(1L, MenuType.DINNER, Category.MAIN_COURSE);

        assertEquals(1, result.size());
    }

    @Test
    void getMenuByTypeAndCategory_invalidCategory() {
        assertThrows(MenuException.class,
                () -> menuService.getMenuByTypeAndCategory(1L, MenuType.DINNER, null));
    }

    // ---------- getMenuByTypeAndDietTypeAndCategory ----------

    @Test
    void getMenuByTypeAndDietTypeAndCategory_success() {
        when(menuRepo.findByBranchIdAndMenuType(1L, MenuType.DINNER))
                .thenReturn(List.of(item));

        List<MenuItem> result =
                menuService.getMenuByTypeAndDietTypeAndCategory(
                        1L, MenuType.DINNER, DietType.VEG, Category.MAIN_COURSE);

        assertEquals(1, result.size());
    }

    @Test
    void getMenuByTypeAndDietTypeAndCategory_invalidInput() {
        assertThrows(MenuException.class,
                () -> menuService.getMenuByTypeAndDietTypeAndCategory(
                        1L, MenuType.DINNER, null, null));
    }
}

