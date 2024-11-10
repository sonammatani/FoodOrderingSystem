package com.project.app.controller;

import com.project.app.dto.GenericApiResponseDto;
import com.project.app.dto.RestaurantInputDto;
import com.project.app.service.RestaurantService;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class RestaurantControllerTest {

    @Mock
    private RestaurantService restaurantService;

    @InjectMocks
    private RestaurantController restaurantController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); 
    }

    @Test
    void testRegisterRestaurant() throws BadRequestException {
        RestaurantInputDto restaurantInputDto = new RestaurantInputDto();
        restaurantInputDto.setName("Test Restaurant");
        restaurantInputDto.setRating(4.5);
        GenericApiResponseDto responseDto
                = new GenericApiResponseDto(HttpStatus.CREATED, "Restaurant registered successfully!");
        restaurantController.registerRestaurant(restaurantInputDto);
        verify(restaurantService, times(1)).registerRestaurant(restaurantInputDto); 
    }

    @Test
    void testUpdateRestaurant() throws BadRequestException {
        RestaurantInputDto restaurantInputDto = new RestaurantInputDto();
        restaurantInputDto.setName("Updated Restaurant");
        restaurantInputDto.setRating(4.7);
        when(restaurantService.updateMenu(restaurantInputDto)).thenReturn(new ResponseEntity<>(restaurantInputDto, HttpStatus.OK));
        ResponseEntity<RestaurantInputDto> response = restaurantController.updateRestaurant(restaurantInputDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(restaurantService, times(1)).updateMenu(restaurantInputDto); 
    }
}
