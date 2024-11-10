package com.project.app.service;

import com.project.app.constants.ConfigConstant;
import com.project.app.dto.GenericApiResponseDto;
import com.project.app.exception.UndeliverableOrderException;
import com.project.app.factory.RestaurantSelectionStrategyFactory;
import com.project.app.javainterface.RestaurantSelectionStrategy;
import com.project.app.model.*;
import com.project.app.repository.ConfigRepository;
import com.project.app.repository.MenuItemRepository;
import com.project.app.repository.OrderRepository;
import com.project.app.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private RestaurantSelectionStrategyFactory restaurantSelectionStrategyFactory;

    @Value("${order.selection.strategy}")
    private String strategy;


    /**
     * place order if the order is deliverable by at least 1 restaurant.
     * else rollback all transactions .
     *
     * @param items items ordered
     * @return items ordered with http response
     * @throws UndeliverableOrderException if any item is undeliverable
     */
    @Transactional(rollbackFor = {UndeliverableOrderException.class, RuntimeException.class})
    public ResponseEntity<GenericApiResponseDto> placeOrder(List<OrderItem> items) throws UndeliverableOrderException {
        OrderData order = orderRepository.save(new OrderData());
        Long orderId = order.getId();
        List<OrderItem> orderItems = new ArrayList<>();
        int totalItems = items.size();
        AtomicInteger successItems = new AtomicInteger();

        String selectedStrategy = getSelectedStrategy(strategy);
        RestaurantSelectionStrategy selectionStrategy =
                restaurantSelectionStrategyFactory.getSelectionStrategy(selectedStrategy);

        boolean isDeliverable = items.stream().allMatch(item -> {
            MenuItem menuItem = menuItemRepository.findByName(item.getItemName());
            if (menuItem == null) return false;  // Item not available

            List<Restaurant> availableRestaurants = restaurantRepository
                    .findAvailableRestaurantsWithItem(item.getItemName(), item.getQuantity());
            //lock eligible restaurants to prevent inconsistency in currentCapacity
            List<Long> restaurantsToLock = availableRestaurants.stream().map(
                    Restaurant::getId).toList();
            availableRestaurants = restaurantRepository.findAllByRestaurantIds(restaurantsToLock);

            if (availableRestaurants.isEmpty()) return false;  // No restaurant with this item

            Restaurant selectedRestaurant = selectionStrategy.selectRestaurant(availableRestaurants, menuItem);
            if (selectedRestaurant == null) return false;  // No restaurant fits the strategy

            reserveCapacity(selectedRestaurant, item.getQuantity());
            successItems.getAndIncrement();
            OrderItem orderItem = new OrderItem();
            orderItem.setItemName(item.getItemName());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setOrderId(orderId);
            orderItems.add(orderItem);
            if(totalItems == successItems.get()) {
                prepareAndDispatchItem(orderItems, selectedRestaurant);
            }
            return true;
        });

        if (!isDeliverable) {
            throw new UndeliverableOrderException("Order cannot be delivered due" +
                    " to unavailable items or capacity issues.");
        }

        order.setDeliverable(isDeliverable);
        order = orderRepository.save(order);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericApiResponseDto(HttpStatus.CREATED, order.toString()));
    }

    private String getSelectedStrategy(String strategy) {
        return configRepository.findByKey(ConfigConstant.RESTAURANT_SELECTION_STRATEGY)
                .map(Config::getValue)
                .orElse(strategy);
    }

    private void reserveCapacity(Restaurant restaurant, int quantity) {
        restaurant.setCurrentCapacity(restaurant.getCurrentCapacity() + quantity);
        restaurantRepository.save(restaurant);
    }

    @Async
    public void prepareAndDispatchItem(List<OrderItem> orderItem, Restaurant restaurant) {
        try {
            for (OrderItem order : orderItem) {
                MenuItem menuItem = menuItemRepository
                        .findByRestaurantIdAndName(restaurant.getId(), order.getItemName());
                int preparationTime = order.getQuantity() * menuItem.getPreparationTime();
                Thread.sleep(preparationTime);

                // After preparation, dispatch the item and release capacity
                dispatchItem(order, restaurant);

            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Preparation interrupted", e);
        }
    }

    private void dispatchItem(OrderItem orderItem, Restaurant restaurant) {
        log.info("Item : {} is dispatched by restaurant : {}" , orderItem.getItemName(), restaurant.getName());
        restaurant.setCurrentCapacity(restaurant.getCurrentCapacity() - orderItem.getQuantity());
        restaurantRepository.save(restaurant);
    }
}
