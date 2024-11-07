package com.project.service;

import com.project.model.MenuItem;
import com.project.model.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HighestRatingStrategyTest {

    private HighestRatingStrategy highestRatingStrategy;

    private MenuItem pizza;

    private Restaurant restaurant1;
    private Restaurant restaurant2;
    private Restaurant restaurant3;

    @BeforeEach
    void setUp() {
        highestRatingStrategy = new HighestRatingStrategy();

        // Create MenuItems
        pizza = new MenuItem();
        pizza.setName("Pizza");
        pizza.setPrice(10.99);

        MenuItem burger = new MenuItem();
        burger.setName("Burger");
        burger.setPrice(5.99);

        // Create Restaurants
        restaurant1 = new Restaurant();
        restaurant1.setName("Restaurant 1");
        restaurant1.setRating(4.2);
        restaurant1.setMenuItems(Arrays.asList(pizza, burger));

        restaurant2 = new Restaurant();
        restaurant2.setName("Restaurant 2");
        restaurant2.setRating(4.5);
        restaurant2.setMenuItems(Collections.singletonList(pizza));

        restaurant3 = new Restaurant();
        restaurant3.setName("Restaurant 3");
        restaurant3.setRating(4.7);
        restaurant3.setMenuItems(Arrays.asList(pizza, burger));
    }

    @Test
    void testSelectRestaurantWithHighestRating() {
        // Arrange
        List<Restaurant> restaurants = Arrays.asList(restaurant1, restaurant2, restaurant3);

        // Act
        Restaurant selectedRestaurant = highestRatingStrategy.selectRestaurant(restaurants, pizza);

        // Assert
        assertNotNull(selectedRestaurant);
        assertEquals("Restaurant 3", selectedRestaurant.getName()); // Restaurant 3 has the highest rating
    }

    @Test
    void testSelectRestaurantWhenNoRestaurantOffersItem() {
        // Arrange
        MenuItem sushi = new MenuItem();
        sushi.setName("Sushi");
        sushi.setPrice(12.99);

        List<Restaurant> restaurants = Arrays.asList(restaurant1, restaurant2, restaurant3);

        // Act
        Restaurant selectedRestaurant = highestRatingStrategy.selectRestaurant(restaurants, sushi);

        // Assert
        assertNull(selectedRestaurant); // No restaurant offers Sushi
    }

    @Test
    void testSelectRestaurantWhenOnlyOneRestaurantOffersItem() {
        // Arrange
        List<Restaurant> restaurants = Collections.singletonList(restaurant2);

        // Act
        Restaurant selectedRestaurant = highestRatingStrategy.selectRestaurant(restaurants, pizza);

        // Assert
        assertNotNull(selectedRestaurant);
        assertEquals("Restaurant 2", selectedRestaurant.getName()); // Only one restaurant offers pizza
    }

    @Test
    void testSelectRestaurantWithMultipleRestaurantsAndSameItem() {
        // Arrange
        // Set ratings for pizza in different restaurants
        pizza.setPrice(10.99);
        restaurant1.setMenuItems(List.of(pizza));
        restaurant1.setRating(3.9);

        pizza.setPrice(10.99);
        restaurant2.setMenuItems(List.of(pizza));
        restaurant2.setRating(4.1);

        pizza.setPrice(10.99);
        restaurant3.setMenuItems(List.of(pizza));
        restaurant3.setRating(4.7);

        List<Restaurant> restaurants = Arrays.asList(restaurant1, restaurant2, restaurant3);

        // Act
        Restaurant selectedRestaurant = highestRatingStrategy.selectRestaurant(restaurants, pizza);

        // Assert
        assertNotNull(selectedRestaurant);
        assertEquals("Restaurant 3", selectedRestaurant.getName()); // Restaurant 3 has the highest rating
    }
}

