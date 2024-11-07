package com.project.service;

import com.project.model.MenuItem;
import com.project.model.Restaurant;
import com.project.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    private Restaurant restaurant;
    private List<MenuItem> menuItems;

    @BeforeEach
    void setUp() {
        // Initialize Mockito mocks
        MockitoAnnotations.openMocks(this);

        // Create test data
        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Test Restaurant");

        // Using no-arg constructor and setter methods
        MenuItem pizza = new MenuItem();
        pizza.setName("Pizza");
        pizza.setPrice(10.99);

        MenuItem burger = new MenuItem();
        burger.setName("Burger");
        burger.setPrice(5.99);

        menuItems = Arrays.asList(pizza, burger);
    }

    @Test
    void testRegisterRestaurant() {
        // Arrange
        when(restaurantRepository.save(restaurant)).thenReturn(restaurant);

        // Act
        Restaurant registeredRestaurant = restaurantService.registerRestaurant(restaurant);

        // Assert
        assertNotNull(registeredRestaurant);
        assertEquals(restaurant.getName(), registeredRestaurant.getName());
        verify(restaurantRepository, times(1)).save(restaurant); // Verify save was called once
    }

    @Test
    void testUpdateMenu() {
        // Arrange
        when(restaurantRepository.findById(restaurant.getId())).thenReturn(Optional.of(restaurant));

        // Act
        restaurantService.updateMenu(restaurant.getId(), menuItems);

        // Assert
        assertEquals(menuItems, restaurant.getMenuItems());
        verify(restaurantRepository, times(1)).save(restaurant); // Verify save was called once
    }

    @Test
    void testUpdateMenuRestaurantNotFound() {
        // Arrange
        when(restaurantRepository.findById(restaurant.getId())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            restaurantService.updateMenu(restaurant.getId(), menuItems);
        });
        assertEquals("Restaurant not found", exception.getMessage());
    }

    @Test
    void testRegisterRestaurantWithNullRestaurant() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            restaurantService.registerRestaurant(null);
        });
        assertEquals("Restaurant cannot be null", exception.getMessage());
    }

    @Test
    void testUpdateMenuWithNullMenuItems() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            restaurantService.updateMenu(restaurant.getId(), null);
        });
        assertEquals("Restaurant ID and menu items cannot be null", exception.getMessage());
    }
}

