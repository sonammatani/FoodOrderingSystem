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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private LowestCostStrategy lowestCostStrategy;

    @Autowired
    private HighestRatingStrategy highestRatingStrategy;

    @Value("${order.selection.strategy}")
    private String strategy;

    public Order placeOrder(List<OrderItem> items) {
        Order order = new Order();
        order.setOrderItems(items);

        boolean deliverable = true; // Start with the assumption that the order is deliverable

        RestaurantSelectionStrategy selectionStrategy = getSelectionStrategy();

        for (OrderItem item : items) {
            // Find the MenuItem by name
            MenuItem menuItem = menuItemRepository.findByName(item.getItemName());

            if (menuItem == null) {
                deliverable = false;
                order.setDeliverable(false);
                break;
            }

            // Fetch available restaurants that have the MenuItem and capacity
            List<Restaurant> availableRestaurants = restaurantRepository.findAvailableRestaurantsForItem(item.getItemName());

            if (availableRestaurants.isEmpty()) {
                // No restaurant can fulfill this item, so the order is undeliverable
                order.setDeliverable(false);
                deliverable = false; // Start with the assumption that the order is deliverable
                break;
            }

            // Select the best restaurant based on the chosen strategy
            Restaurant selectedRestaurant = selectionStrategy.selectRestaurant(availableRestaurants, menuItem);

            if (selectedRestaurant == null) {
                order.setDeliverable(false);
                deliverable = false;
                break;
            } else {
                // Reserve capacity for the restaurant
                selectedRestaurant.setCurrentCapacity(selectedRestaurant.getCurrentCapacity() + item.getQuantity());
                restaurantRepository.save(selectedRestaurant);

                prepareAndDispatchItem(item, selectedRestaurant);
            }
        }

        // If all items are deliverable, mark the order as deliverable
        order.setDeliverable(deliverable);
        return orderRepository.save(order);
    }

    private RestaurantSelectionStrategy getSelectionStrategy() {
        return "highestRating".equalsIgnoreCase(strategy) ? highestRatingStrategy : lowestCostStrategy;
    }

    @Async
    public CompletableFuture<Void> prepareAndDispatchItem(OrderItem orderItem, Restaurant restaurant) {
        try {
            // Simulate preparation time (e.g., 5 seconds per item)
            int preparationTime = orderItem.getQuantity() * 5000;
            Thread.sleep(preparationTime);

            // After preparation, dispatch the item and release capacity
            dispatchItem(orderItem, restaurant);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Preparation interrupted", e);
        }

        return CompletableFuture.completedFuture(null);
    }

    private void dispatchItem(OrderItem orderItem, Restaurant restaurant) {
        // Logic to dispatch the item
        System.out.println("Item " + orderItem.getItemName() + " dispatched by restaurant " + restaurant.getName());

        // Release processing capacity
        restaurant.setCurrentCapacity(restaurant.getCurrentCapacity() - orderItem.getQuantity());
        restaurantRepository.save(restaurant);
    }
}
