package com.project.service;

import com.project.javainterface.RestaurantSelectionStrategy;
import com.project.model.MenuItem;
import com.project.model.Restaurant;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class HighestRatingStrategy implements RestaurantSelectionStrategy {
    @Override
    public Restaurant selectRestaurant(List<Restaurant> restaurants, MenuItem item) {
        return restaurants.stream()
                .filter(r -> r.getMenuItems().contains(item))
                .max(Comparator.comparingDouble(Restaurant::getRating))
                .orElse(null);
    }
}
