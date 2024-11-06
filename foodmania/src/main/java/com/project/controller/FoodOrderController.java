package com.project.controller;

import com.project.model.Order;
import com.project.model.OrderItem;
import com.project.model.Restaurant;
import com.project.service.OrderService;
import com.project.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FoodOrderController {
    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private OrderService orderService;

    @PostMapping("/restaurants")
    public Restaurant registerRestaurant(@RequestBody Restaurant restaurant) {
        return restaurantService.registerRestaurant(restaurant);
    }

    @PostMapping("/orders")
    public Order placeOrder(@RequestBody List<OrderItem> items) {
        return orderService.placeOrder(items);
    }
}

