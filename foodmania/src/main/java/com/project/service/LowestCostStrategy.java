package com.project.service;

import com.project.javainterface.RestaurantSelectionStrategy;
import com.project.model.MenuItem;
import com.project.model.Restaurant;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class LowestCostStrategy implements RestaurantSelectionStrategy {
    @Override
    public Restaurant selectRestaurant(List<Restaurant> restaurants, MenuItem targetItem) {
        return restaurants.stream()
                .filter(restaurant -> restaurant.getMenuItems().stream()
                        .anyMatch(menuItem -> menuItem.getName().equals(targetItem.getName())))
                .min(Comparator.comparingDouble(restaurant ->
                        restaurant.getMenuItems().stream()
                                .filter(menuItem -> menuItem.getName().equals(targetItem.getName()))
                                .findFirst()
                                .map(MenuItem::getPrice)
                                .orElse(Double.MAX_VALUE)))
                .orElse(null);
    }
}
