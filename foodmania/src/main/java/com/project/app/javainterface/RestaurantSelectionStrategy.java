package com.project.app.javainterface;

import com.project.app.model.MenuItem;
import com.project.app.model.Restaurant;

import java.util.List;

public interface RestaurantSelectionStrategy {
    Restaurant selectRestaurant(List<Restaurant> restaurants, MenuItem item);
}
