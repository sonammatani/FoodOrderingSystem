package com.project.app.service;

import com.project.app.dto.GenericApiResponseDto;
import com.project.app.dto.RestaurantInputDto;
import com.project.app.model.MenuItem;
import com.project.app.model.Restaurant;
import com.project.app.repository.MenuItemRepository;
import com.project.app.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private MenuItemRepository menuItemRepository;
    @Autowired
    private MenuItemService menuItemService;

    /**
     * saves new restaurant, if restaurant with same name does not exist.
     *
     * @param restaurantDto has restaurant data and menu items
     * @return saved restaurant and menu items
     */
    public ResponseEntity<GenericApiResponseDto> registerRestaurant(RestaurantInputDto restaurantDto) {
        //validate restaurant
        if(isValidRestaurantData(restaurantDto)) {
            Restaurant restaurant = buildRestaurantData(restaurantDto);
            Restaurant savedRestaurant = restaurantRepository.save(restaurant);
            menuItemService.addMenuItem(savedRestaurant.getId(), restaurantDto.getMenuItems());
            restaurantDto.setId(savedRestaurant.getId());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new GenericApiResponseDto(HttpStatus.CREATED, restaurantDto.toString()));
        } else {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new GenericApiResponseDto(HttpStatus.BAD_REQUEST,
                            "Restaurant with this name is already registered."));
        }
    }

    /**
     * builds restaurant data from input dto to save in database.
     *
     * @param restaurantDto has restaurant data and menu items
     * @return Restaurant Object
     */
    private static Restaurant buildRestaurantData(RestaurantInputDto restaurantDto) {
        return Restaurant.builder()
                .name(restaurantDto.getName())
                .rating(restaurantDto.getRating())
                .currentCapacity(restaurantDto.getCurrentCapacity())
                .maxCapacity(restaurantDto.getMaxCapacity())
                .build();
    }

    /**
     * validate if restaurant is already registered.
     *
     * @param restaurantDto has restaurant data and menu items
     * @return true if restaurant is not present else false
     */
    private boolean isValidRestaurantData(RestaurantInputDto restaurantDto) {
        return restaurantRepository.findByName(restaurantDto.getName()) == null;
    }

    /**
     * update restaurant data or menu items as per the input.
     *
     * @param restaurantDto has restaurant data and menu items
     * @return updated restaurant data
     */
    public ResponseEntity<GenericApiResponseDto> updateMenu(RestaurantInputDto restaurantDto) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantDto.getId());
        if (restaurant.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new GenericApiResponseDto(HttpStatus.BAD_REQUEST,
                            "Restaurant is not registered."));
        }
        Restaurant updatedRestaurantData = buildRestaurantData(restaurantDto);
        updatedRestaurantData.setId(restaurant.get().getId());
        Restaurant savedRestaurant =  restaurantRepository.save(updatedRestaurantData);

        List<MenuItem> menuItems = restaurantDto.getMenuItems();
        List<MenuItem> savedMenuItem = menuItemRepository.saveAll(menuItems);

        restaurantDto.setId(savedRestaurant.getId());
        restaurantDto.setMenuItems(savedMenuItem);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new GenericApiResponseDto(HttpStatus.CREATED, restaurantDto.toString()));
    }

}
