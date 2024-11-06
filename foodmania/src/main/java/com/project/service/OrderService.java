package com.project.service;

import com.project.javainterface.RestaurantSelectionStrategy;
import com.project.model.MenuItem;
import com.project.model.Order;
import com.project.model.OrderItem;
import com.project.model.Restaurant;
import com.project.repository.MenuItemRepository;
import com.project.repository.OrderRepository;
import com.project.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private LowestCostStrategy lowestCostStrategy;

    @Autowired
    private HighestRatingStrategy highestRatingStrategy;

    @Value("${order.selection.strategy}")
    private String strategy;

    public Order placeOrder(List<OrderItem> items) {
        Order order = new Order();
        order.setOrderItems(items);

        RestaurantSelectionStrategy selectionStrategy = getSelectionStrategy();

        for (OrderItem item : items) {
            List<Restaurant> availableRestaurants = restaurantRepository.findAvailableRestaurantsForItem(item.getItemName());
            MenuItem menuItem = menuItemRepository.findByName(item.getItemName());

            Restaurant selectedRestaurant = selectionStrategy.selectRestaurant(availableRestaurants, menuItem);

            if (selectedRestaurant == null) {
                order.setDeliverable(false);
                break;
            } else {
                selectedRestaurant.setCurrentCapacity(selectedRestaurant.getCurrentCapacity() + item.getQuantity());
                restaurantRepository.save(selectedRestaurant);
            }
        }

        order.setDeliverable(true);
        return orderRepository.save(order);
    }

    private RestaurantSelectionStrategy getSelectionStrategy() {
        return "highestRating".equalsIgnoreCase(strategy) ? highestRatingStrategy : lowestCostStrategy;
    }
}
