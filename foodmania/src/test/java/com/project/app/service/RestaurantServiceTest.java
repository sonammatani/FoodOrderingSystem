package com.project.app.service;

import com.project.app.dto.RestaurantInputDto;
import com.project.app.model.MenuItem;
import com.project.app.model.Restaurant;
import com.project.app.repository.MenuItemRepository;
import com.project.app.repository.RestaurantRepository;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
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
    private List<MenuItem> menuItems;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up test data
        restaurantDto = new RestaurantInputDto();
        restaurantDto.setName("Test Restaurant");
        restaurantDto.setRating(4.5);
        restaurantDto.setCurrentCapacity(50);
        restaurantDto.setMaxCapacity(100);
        restaurantDto.setMenuItems(new ArrayList<>());

        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Test Restaurant");
        restaurant.setRating(4.5);
        restaurant.setCurrentCapacity(50);
        restaurant.setMaxCapacity(100);

        menuItems = new ArrayList<>();
        MenuItem menuItem = new MenuItem();
        menuItem.setId(1L);
        menuItem.setName("Burger");
        menuItem.setPrice(5.99);
        menuItem.setPreparationTime(15);
        menuItems.add(menuItem);
    }

    @Test
    void testRegisterRestaurant_Success() throws BadRequestException {
        when(restaurantRepository.findByName(restaurantDto.getName())).thenReturn(null);
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);
        doNothing().when(menuItemService).addMenuItem(anyLong(), anyList());
        ResponseEntity<RestaurantInputDto> response = restaurantService.registerRestaurant(restaurantDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(restaurantDto.getName(), response.getBody().getName());
        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
    }

    @Test
    void testRegisterRestaurant_Failure_AlreadyRegistered() {
        // Arrange
        when(restaurantRepository.findByName(restaurantDto.getName())).thenReturn(restaurant);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            restaurantService.registerRestaurant(restaurantDto);
        });
        assertEquals("Restaurant is already registered.", exception.getMessage());
    }

    @Test
    void testUpdateMenu_Success() throws BadRequestException {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);
        when(menuItemRepository.saveAll(anyList())).thenReturn(menuItems);

        restaurantDto.setId(1L);
        ResponseEntity<RestaurantInputDto> response = restaurantService.updateMenu(restaurantDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(restaurantDto.getName(), response.getBody().getName());
        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
        verify(menuItemRepository, times(1)).saveAll(anyList());
    }


    @Test
    void testUpdateMenu_Failure_RestaurantNotFound() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            restaurantService.updateMenu(restaurantDto);
        });
        assertEquals("No restaurant is registered with name Test Restaurant.", exception.getMessage());
    }
}
