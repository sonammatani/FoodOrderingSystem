package com.project.service;

import com.project.model.MenuItem;
import com.project.model.Restaurant;
import com.project.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RestaurantService {
    @Autowired
    private RestaurantRepository restaurantRepository;

    public Restaurant registerRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    public void updateMenu(Long restaurantId, List<MenuItem> menuItems) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow();
        restaurant.setMenuItems(menuItems);
        restaurantRepository.save(restaurant);
    }
}
