package com.project.app.service;

import com.project.app.dto.RestaurantSelectionDto;
import com.project.app.javainterface.RestaurantSelectionStrategy;
import com.project.app.model.MenuItem;
import com.project.app.model.Restaurant;
import com.project.app.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class LowestCostStrategy implements RestaurantSelectionStrategy {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public Restaurant selectRestaurant(List<Restaurant> restaurants, MenuItem targetItem) {

        Long restaurantId;
        List<Long> restaurantIds = restaurants.stream().map(Restaurant::getId).toList();
        List<RestaurantSelectionDto> restaurantSelectionDto = restaurantRepository
                .findDataForEligibleRestaurants(restaurantIds, targetItem.getName());

        restaurantId = restaurantSelectionDto.stream()
                .min(Comparator.comparingDouble(RestaurantSelectionDto::getItemPrice))
                .map(RestaurantSelectionDto::getRestaurantId)
                .orElse(null);

        Optional<Restaurant> restaurant = restaurantRepository.findById(
                restaurantId == null ? 0L : restaurantId);
        return restaurant.orElse(null);

    }
}