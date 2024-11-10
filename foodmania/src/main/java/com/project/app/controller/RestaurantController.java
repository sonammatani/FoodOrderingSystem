package com.project.app.controller;

import com.project.app.dto.GenericApiResponseDto;
import com.project.app.dto.RestaurantInputDto;
import com.project.app.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping("/addRestaurant")
    public ResponseEntity<GenericApiResponseDto> registerRestaurant(@RequestBody RestaurantInputDto restaurantDto) {
        return restaurantService.registerRestaurant(restaurantDto);
    }

    @PostMapping("/updateRestaurant")
    public ResponseEntity<GenericApiResponseDto> updateRestaurant(@RequestBody RestaurantInputDto restaurantDto) {
        return restaurantService.updateMenu(restaurantDto);
    }
}