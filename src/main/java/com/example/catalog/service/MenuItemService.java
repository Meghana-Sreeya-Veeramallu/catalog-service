package com.example.catalog.service;

import com.example.catalog.Exceptions.MenuItemAlreadyExistsException;
import com.example.catalog.Exceptions.MenuItemNotFoundException;
import com.example.catalog.Exceptions.RestaurantNotFoundException;
import com.example.catalog.model.MenuItem;
import com.example.catalog.model.Restaurant;
import com.example.catalog.repository.MenuItemRepository;
import com.example.catalog.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuItemService {
    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;

    @Autowired
    public MenuItemService(MenuItemRepository menuItemRepository, RestaurantRepository restaurantRepository) {
        this.menuItemRepository = menuItemRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public void addMenuItem(Long restaurantId, String itemName, double price) {
        Restaurant restaurant = findRestaurantById(restaurantId);
        if (menuItemRepository.findByNameAndRestaurantId(itemName, restaurantId).isPresent()) {
            throw new MenuItemAlreadyExistsException("Menu item '" + itemName + "' already exists for restaurant with ID '" + restaurantId + "'");
        }

        MenuItem menuItem = restaurant.addMenuItem(itemName, price);
        menuItemRepository.save(menuItem);
    }

    public List<MenuItem> getAllMenuItems(Long restaurantId) {
        findRestaurantById(restaurantId);
        return menuItemRepository.findByRestaurantId(restaurantId);
    }

    public MenuItem getMenuItemById(Long menuItemId, Long restaurantId) {
        findRestaurantById(restaurantId);
        return menuItemRepository.findByIdAndRestaurantId(menuItemId, restaurantId)
                .orElseThrow(() -> new MenuItemNotFoundException("Menu item with ID '" + menuItemId + "' not found for restaurant with ID '" + restaurantId + "'"));
    }

    private Restaurant findRestaurantById(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant with ID '" + restaurantId + "' not found"));
    }
}
