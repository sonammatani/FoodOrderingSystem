package com.project.service;

import com.project.model.MenuItem;
import com.project.model.Order;
import com.project.model.OrderItem;
import com.project.model.Restaurant;
import com.project.repository.MenuItemRepository;
import com.project.repository.OrderRepository;
import com.project.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private LowestCostStrategy lowestCostStrategy;

    @Mock
    private HighestRatingStrategy highestRatingStrategy;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPlaceOrder_ItemNotFound() {
        // Arrange
        OrderItem orderItem = new OrderItem(1L,"Pizza", 2);
        List<OrderItem> orderItems = List.of(orderItem);

        when(menuItemRepository.findByName("Pizza")).thenReturn(null); // Simulate item not found

        // Act
        Order order = orderService.placeOrder(orderItems);

        // Assert
        assertFalse(order.isDeliverable());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testPlaceOrder_NoAvailableRestaurant() {
        // Arrange
        OrderItem orderItem = new OrderItem(2L,"Burger", 3);
        List<OrderItem> orderItems = List.of(orderItem);

        MenuItem menuItem = new MenuItem();
        menuItem.setPreparationTime(5);
        menuItem.setName("Burger");
        when(menuItemRepository.findByName("Burger")).thenReturn(menuItem); // Simulate item exists

        when(restaurantRepository.findAvailableRestaurantsForItem("Burger"))
                .thenReturn(List.of()); // No available restaurant for this item

        // Act
        Order order = orderService.placeOrder(orderItems);

        // Assert
        assertFalse(order.isDeliverable());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testPlaceOrder_SuccessfulOrder() {
        // Arrange
        OrderItem orderItem = new OrderItem();
        orderItem.setItemName("Pizza");
        orderItem.setQuantity(2);
        List<OrderItem> orderItems = List.of(orderItem);

        MenuItem menuItem = new MenuItem();
        menuItem.setPreparationTime(5);
        menuItem.setName("Pizza");
        when(menuItemRepository.findByName("Pizza")).thenReturn(menuItem); // Simulate item exists

        Restaurant restaurant = new Restaurant();
        restaurant.setName("Restaurant A");
        restaurant.setRating(10);
        when(restaurantRepository.findAvailableRestaurantsForItem("Pizza"))
                .thenReturn(List.of(restaurant)); // Available restaurant for this item

        when(lowestCostStrategy.selectRestaurant(any(), eq(menuItem))).thenReturn(restaurant); // Simulate restaurant selection strategy

        // Act
        Order order = orderService.placeOrder(orderItems);

        // Assert
        assertTrue(order.isDeliverable());
        verify(orderRepository, times(1)).save(order);
        verify(restaurantRepository, times(1)).save(restaurant); // Ensure restaurant capacity is updated
    }

    @Test
    void testPrepareAndDispatchItem() throws Exception {
        // Arrange
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(1);
        orderItem.setItemName("Burger");
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Restaurant B");
        restaurant.setRating(10);

        // Act
        CompletableFuture<Void> future = orderService.prepareAndDispatchItem(orderItem, restaurant);

        // Assert
        assertNotNull(future);
        verify(restaurantRepository, times(1)).save(restaurant); // Ensure restaurant capacity is updated after dispatch
    }
}

