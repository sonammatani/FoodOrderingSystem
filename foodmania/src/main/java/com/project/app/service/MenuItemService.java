package com.project.app.service;

import com.project.app.model.MenuItem;
import com.project.app.repository.MenuItemRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class MenuItemService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    /**
     * save menu items provided during restaurant registration and update action.
     *
     * @param restaurantId restaurantId
     * @param menuItems menuItems
     */
    public void addMenuItem(Long restaurantId, List<MenuItem> menuItems) {
        List<MenuItem> menuItemList = menuItems.stream()
                .peek(menuItem -> menuItem.setRestaurantId(restaurantId))
                .collect(Collectors.toList());
        menuItemRepository.saveAll(menuItemList);
    }


    /**
     * cache method to hold menu items and reduce database overload.
     *
     * @return all unique menu items
     */
    @Cacheable(value = "menuItems")
    public List<String> getMenuItems() {
        return menuItemRepository.findAllUniqueMenuItems();
    }
}
