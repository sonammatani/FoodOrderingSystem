package com.project.javainterface;

import com.project.model.MenuItem;
import com.project.model.Restaurant;

import java.util.List;

public interface RestaurantSelectionStrategy {
    Restaurant selectRestaurant(List<Restaurant> restaurants, MenuItem item);
}
