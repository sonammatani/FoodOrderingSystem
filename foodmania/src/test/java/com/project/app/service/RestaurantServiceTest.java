package com.project.app.service;

import com.project.app.dto.GenericApiResponseDto;
import com.project.app.dto.RestaurantInputDto;
import com.project.app.model.MenuItem;
import com.project.app.model.Restaurant;
import com.project.app.repository.MenuItemRepository;
import com.project.app.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class RestaurantServiceTest {

    @InjectMocks
    private RestaurantService restaurantService;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private MenuItemService menuItemService;

    private RestaurantInputDto restaurantDto;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        restaurantDto = new RestaurantInputDto();
        restaurantDto.setName("Test Restaurant");
        restaurantDto.setRating(4.5);
        restaurantDto.setMaxCapacity(100);
        restaurantDto.setCurrentCapacity(50);

        restaurant = Restaurant.builder()
                .id(1L)
                .name("Test Restaurant")
                .rating(4.5)
                .maxCapacity(100)
                .currentCapacity(50)
                .build();
    }

    @Test
    void testRegisterRestaurantSuccess() {
        List<MenuItem> mockMenuItems = Arrays.asList(new MenuItem(), new MenuItem());
        restaurantDto.setMenuItems(mockMenuItems);

        when(restaurantRepository.findByName(restaurantDto.getName())).thenReturn(null);
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);
        
        ResponseEntity<GenericApiResponseDto> response = restaurantService.registerRestaurant(restaurantDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).
                getMessage().contains("Test Restaurant"));
        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
        verify(menuItemService, times(1)).addMenuItem(eq(restaurant.getId()), anyList());
    }


    @Test
    void testRegisterRestaurantFailureDueToExistingName() {
        when(restaurantRepository.findByName(restaurantDto.getName())).thenReturn(restaurant);
        ResponseEntity<GenericApiResponseDto> response = restaurantService.registerRestaurant(restaurantDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody())
                .getMessage().contains("already registered"));
        verify(restaurantRepository, never()).save(any(Restaurant.class));
        verify(menuItemService, never()).addMenuItem(anyLong(), anyList());
    }

    @Test
    void testUpdateMenuSuccess() {
        restaurantDto.setId(1L);
        MenuItem menuItem = new MenuItem();
        menuItem.setName("Pizza");
        restaurantDto.setMenuItems(List.of(menuItem));

        when(restaurantRepository.findById(restaurantDto.getId())).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);
        when(menuItemRepository.saveAll(anyList())).thenReturn(restaurantDto.getMenuItems());

        ResponseEntity<GenericApiResponseDto> response = restaurantService.updateMenu(restaurantDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody())
                .getMessage().contains("Test Restaurant"));
        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
        verify(menuItemRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testUpdateMenuRestaurantNotFound() {
        restaurantDto.setId(1L);
        when(restaurantRepository.findById(restaurantDto.getId())).thenReturn(Optional.empty());

        ResponseEntity<GenericApiResponseDto> response = restaurantService.updateMenu(restaurantDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody())
                .getMessage().contains("Restaurant is not registered"));
        verify(restaurantRepository, never()).save(any(Restaurant.class));
        verify(menuItemRepository, never()).saveAll(anyList());
    }
}
