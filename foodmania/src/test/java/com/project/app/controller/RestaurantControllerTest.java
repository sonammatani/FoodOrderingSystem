package com.project.app.controller;

import com.project.app.dto.GenericApiResponseDto;
import com.project.app.dto.RestaurantInputDto;
import com.project.app.service.RestaurantService;
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
    void testRegisterRestaurant() {
        RestaurantInputDto restaurantInputDto = new RestaurantInputDto();
        restaurantInputDto.setName("Test Restaurant");
        restaurantInputDto.setRating(4.5);
        GenericApiResponseDto responseDto
                = new GenericApiResponseDto(HttpStatus.CREATED, "Restaurant registered successfully!");
        when(restaurantService.registerRestaurant(restaurantInputDto))
                .thenReturn(new ResponseEntity<>(responseDto, HttpStatus.CREATED));
        ResponseEntity<GenericApiResponseDto> response = restaurantController.registerRestaurant(restaurantInputDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Restaurant registered successfully!",
                Objects.requireNonNull(response.getBody()).getMessage());
        verify(restaurantService, times(1)).registerRestaurant(restaurantInputDto); 
    }

    @Test
    void testUpdateRestaurant() {
        RestaurantInputDto restaurantInputDto = new RestaurantInputDto();
        restaurantInputDto.setName("Updated Restaurant");
        restaurantInputDto.setRating(4.7);
        GenericApiResponseDto responseDto = new GenericApiResponseDto(HttpStatus.OK, "Restaurant updated successfully!");
        when(restaurantService.updateMenu(restaurantInputDto)).thenReturn(new ResponseEntity<>(responseDto, HttpStatus.OK));
        ResponseEntity<GenericApiResponseDto> response = restaurantController.updateRestaurant(restaurantInputDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Restaurant updated successfully!",
                Objects.requireNonNull(response.getBody()).getMessage());
        verify(restaurantService, times(1)).updateMenu(restaurantInputDto); 
    }
}
