package com.project.app.service;

import com.project.app.dto.GenericApiResponseDto;
import com.project.app.exception.UndeliverableOrderException;
import com.project.app.model.MenuItem;
import com.project.app.model.OrderData;
import com.project.app.model.OrderItem;
import com.project.app.model.Restaurant;
import com.project.app.repository.MenuItemRepository;
import com.project.app.repository.OrderRepository;
import com.project.app.repository.RestaurantRepository;
import com.project.app.factory.RestaurantSelectionStrategyFactory;
import com.project.app.javainterface.RestaurantSelectionStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private RestaurantSelectionStrategyFactory restaurantSelectionStrategyFactory;

    @Mock
    private RestaurantSelectionStrategy restaurantSelectionStrategy;

    @Mock
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(orderService, "strategy", "lowestCost");
    }

    @Test
    void testPlaceOrder_Success() throws UndeliverableOrderException {
        OrderItem item1 = new OrderItem();
        item1.setItemName("Pizza");
        item1.setQuantity(2);
        List<OrderItem> items =List.of(item1);

        MenuItem menuItem = new MenuItem();
        menuItem.setName("Pizza");
        menuItem.setPreparationTime(20);
        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setCurrentCapacity(10);
        when(menuItemRepository.findByName(anyString())).thenReturn(menuItem);
        when(menuItemRepository.findByRestaurantIdAndName(any(), any()))
                .thenReturn(menuItem);
        when(restaurantRepository.findAvailableRestaurantsWithItem(anyString(), anyInt()))
                .thenReturn(Collections.singletonList(restaurant));
        when(restaurantRepository.findAllByRestaurantIds(any()))
                        .thenReturn(List.of(restaurant));
        when(restaurantSelectionStrategyFactory.getSelectionStrategy(anyString()))
                .thenReturn(restaurantSelectionStrategy);
        when(restaurantSelectionStrategy.selectRestaurant(any(), any())).thenReturn(restaurant);

        OrderData mockOrder = new OrderData();
        mockOrder.setId(1L);
        when(orderRepository.save(any(OrderData.class))).thenReturn(mockOrder);

        ResponseEntity<GenericApiResponseDto> response = orderService.placeOrder(items);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(orderRepository, times(2)).save(any());
        verify(restaurantRepository, times(2)).save(restaurant);
    }



    @Test
    void testPlaceOrder_Failure_ItemNotAvailable() {
        OrderData mockOrder = new OrderData();
        mockOrder.setId(1L);
        when(orderRepository.save(any(OrderData.class))).thenReturn(mockOrder);
        OrderItem item1 = new OrderItem();
        item1.setItemName("Pizza");
        item1.setQuantity(2);
        List<OrderItem> items =List.of(item1);

        when(menuItemRepository.findByName(anyString())).thenReturn(null);

        assertThrows(UndeliverableOrderException.class, () -> orderService.placeOrder(items));
    }

    @Test
    void testPlaceOrder_Failure_InsufficientCapacity() {
        OrderItem item1 = new OrderItem();
        item1.setItemName("Pizza");
        item1.setQuantity(20);
        List<OrderItem> items =List.of(item1);

        when(menuItemRepository.findByName(anyString())).thenReturn(new MenuItem());
        when(restaurantRepository.findAvailableRestaurantsWithItem(anyString(), anyInt()))
                .thenReturn(Collections.singletonList(restaurant));
        when(restaurantSelectionStrategyFactory.getSelectionStrategy(anyString()))
                .thenReturn(restaurantSelectionStrategy);
        when(restaurantSelectionStrategy.selectRestaurant(any(), any())).thenReturn(restaurant);
        OrderData mockOrder = new OrderData();
        mockOrder.setId(1L);
        when(orderRepository.save(any(OrderData.class))).thenReturn(mockOrder);
        when(restaurant.getCurrentCapacity()).thenReturn(10);

        assertThrows(UndeliverableOrderException.class, () -> orderService.placeOrder(items));
    }


    @Test
    void testPlaceOrder_Failure_RestaurantSelectionStrategyReturnsNull() {
        OrderData mockOrder = new OrderData();
        mockOrder.setId(1L);
        when(orderRepository.save(any(OrderData.class))).thenReturn(mockOrder);
        OrderItem item1 = new OrderItem();
        item1.setItemName("Pizza");
        item1.setQuantity(2);
        List<OrderItem> items =List.of(item1);

        when(menuItemRepository.findByName(anyString())).thenReturn(new MenuItem());
        when(restaurantRepository.findAvailableRestaurantsWithItem(anyString(), anyInt()))
                .thenReturn(Collections.singletonList(restaurant));
        when(restaurantSelectionStrategyFactory.getSelectionStrategy(anyString()))
                .thenReturn(restaurantSelectionStrategy);
        when(restaurantSelectionStrategy.selectRestaurant(any(), any())).thenReturn(null);

        assertThrows(UndeliverableOrderException.class, () -> orderService.placeOrder(items));
    }
}
