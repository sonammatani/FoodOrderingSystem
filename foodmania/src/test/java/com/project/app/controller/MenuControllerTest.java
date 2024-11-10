package com.project.app.controller;

import com.project.app.service.MenuItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class MenuControllerTest {

    @Mock
    private MenuItemService menuItemService;

    @InjectMocks
    private MenuController menuController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); 
    }

    @Test
    void testGetAllMenuItems() {
        List<String> menuItems = Arrays.asList("Pasta", "Pizza", "Burger");
        when(menuItemService.getMenuItems()).thenReturn(menuItems);

        List<String> response = menuController.getAllMenuItems();

        assertNotNull(response);
        assertEquals(3, response.size());
        assertEquals("Pasta", response.get(0));
        assertEquals("Pizza", response.get(1));
        assertEquals("Burger", response.get(2));
        verify(menuItemService, times(1)).getMenuItems(); 
    }
}
