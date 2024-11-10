package com.project.app.service;

import com.project.app.dto.RestaurantSelectionDto;
import com.project.app.model.MenuItem;
import com.project.app.model.Restaurant;
import com.project.app.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Add this annotation
class LowestCostStrategyTest {

    @InjectMocks
    private LowestCostStrategy lowestCostStrategy;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Test
    void testSelectRestaurant() {

        MenuItem targetItem = new MenuItem();
        targetItem.setName("Burger");

        Restaurant restaurant1 = new Restaurant();
        restaurant1.setId(1L);
        restaurant1.setName("Restaurant A");

        Restaurant restaurant2 = new Restaurant();
        restaurant2.setId(2L);
        restaurant2.setName("Restaurant B");

        RestaurantSelectionDto dto1 = new RestaurantSelectionDto();
        dto1.setRestaurantId(1L);
        dto1.setItemPrice(5.99);

        RestaurantSelectionDto dto2 = new RestaurantSelectionDto();
        dto2.setRestaurantId(2L);
        dto2.setItemPrice(3.99);

        List<Restaurant> restaurants = Arrays.asList(restaurant1, restaurant2);
        List<RestaurantSelectionDto> restaurantSelectionDtos = Arrays.asList(dto1, dto2);


        when(restaurantRepository.findDataForEligibleRestaurants(List.of(1L, 2L), "Burger"))
                .thenReturn(restaurantSelectionDtos);
        when(restaurantRepository.findById(2L)).thenReturn(Optional.of(restaurant2)); // Mock the lowest cost restaurant

        Restaurant selectedRestaurant = lowestCostStrategy.selectRestaurant(restaurants, targetItem);

        assertNotNull(selectedRestaurant);
        assertEquals(2L, selectedRestaurant.getId());
        assertEquals("Restaurant B", selectedRestaurant.getName());

        verify(restaurantRepository).findDataForEligibleRestaurants(List.of(1L, 2L), "Burger");
        verify(restaurantRepository).findById(2L);
    }

    @Test
    void testSelectRestaurant_NoEligibleRestaurant() {

        MenuItem targetItem = new MenuItem();
        targetItem.setName("Pizza");

        Restaurant restaurant1 = new Restaurant();
        restaurant1.setId(1L);
        restaurant1.setName("Restaurant A");

        List<Restaurant> restaurants = List.of(restaurant1);
        List<RestaurantSelectionDto> restaurantSelectionDtos = List.of();

        when(restaurantRepository.findDataForEligibleRestaurants(List.of(1L), "Pizza"))
                .thenReturn(restaurantSelectionDtos);

        Restaurant selectedRestaurant = lowestCostStrategy.selectRestaurant(restaurants, targetItem);

        assertNull(selectedRestaurant);
        verify(restaurantRepository).findDataForEligibleRestaurants(List.of(1L), "Pizza");
    }
}
