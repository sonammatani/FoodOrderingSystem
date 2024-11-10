package com.project.app.controller;

import com.project.app.dto.GenericApiResponseDto;
import com.project.app.exception.UndeliverableOrderException;
import com.project.app.model.OrderItem;
import com.project.app.service.OrderService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FoodOrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<List<OrderItem>> placeOrder(@RequestBody List<OrderItem> items) throws UndeliverableOrderException {
            return orderService.placeOrder(items);
    }
}

