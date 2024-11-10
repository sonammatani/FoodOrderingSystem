package com.project.app.service;

import com.project.app.model.MenuItem;
import com.project.app.repository.MenuItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MenuItemServiceTest {

    @InjectMocks
    private MenuItemService menuItemService;

    @Mock
    private MenuItemRepository menuItemRepository;

    @Test
    void testAddMenuItem() {
        Long restaurantId = 1L;
        MenuItem item1 = new MenuItem();
        item1.setName("Pizza");
        MenuItem item2 = new MenuItem();
        item2.setName("Burger");
        List<MenuItem> menuItems = Arrays.asList(item1, item2);

        menuItemService.addMenuItem(restaurantId, menuItems);

        assertEquals(restaurantId, item1.getRestaurantId());
        assertEquals(restaurantId, item2.getRestaurantId());

        verify(menuItemRepository).saveAll(menuItems);
    }

    @Test
    void testGetMenuItems() {
        List<String> menuItems = Arrays.asList("Pizza", "Burger");

        when(menuItemRepository.findAllUniqueMenuItems()).thenReturn(menuItems);

        List<String> result = menuItemService.getMenuItems();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("Pizza"));
        assertTrue(result.contains("Burger"));

        verify(menuItemRepository, times(1)).findAllUniqueMenuItems();
    }
}
