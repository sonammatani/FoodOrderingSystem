package com.project.controller;

import com.project.model.MenuItem;
import com.project.repository.MenuItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class MenuControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private MenuController menuController;

    private MenuItem pizza;
    private MenuItem burger;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(menuController).build();

        // Set up test data
        pizza = new MenuItem();
        pizza.setName("Pizza");
        pizza.setPrice(10.99);

        burger = new MenuItem();
        burger.setName("Burger");
        burger.setPrice(5.99);
    }

    @Test
    void testGetAllMenuItems() throws Exception {
        // Arrange
        List<MenuItem> menuItems = Arrays.asList(pizza, burger);
        when(menuItemRepository.findAll()).thenReturn(menuItems);

        // Act & Assert
        mockMvc.perform(get("/api/menu/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Pizza"))
                .andExpect(jsonPath("$[0].price").value(10.99))
                .andExpect(jsonPath("$[1].name").value("Burger"))
                .andExpect(jsonPath("$[1].price").value(5.99));
    }

    @Test
    void testGetAllUniqueMenuItems() throws Exception {
        // Arrange
        List<String> uniqueMenuItems = Arrays.asList("Pizza", "Burger");
        when(menuItemRepository.findAllUniqueMenuItems()).thenReturn(uniqueMenuItems);

        // Act & Assert
        mockMvc.perform(get("/api/menu/unique-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Pizza"))
                .andExpect(jsonPath("$[1]").value("Burger"));
    }
}

