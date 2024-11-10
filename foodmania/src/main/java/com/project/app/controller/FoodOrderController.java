package com.project.app.controller;

import com.project.app.dto.GenericApiResponseDto;
import com.project.app.exception.UndeliverableOrderException;
import com.project.app.model.OrderItem;
import com.project.app.service.OrderService;
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
    public ResponseEntity<GenericApiResponseDto> placeOrder(@RequestBody List<OrderItem> items) {
        try {
            return orderService.placeOrder(items);
        } catch (UndeliverableOrderException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new GenericApiResponseDto(HttpStatus.BAD_REQUEST,
                            "Order could not be placed: " + ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GenericApiResponseDto(HttpStatus.BAD_REQUEST,
                            "An unexpected error occurred while placing the order."));
        }
    }
}

