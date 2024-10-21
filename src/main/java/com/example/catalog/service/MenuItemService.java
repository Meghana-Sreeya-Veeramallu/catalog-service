package com.example.catalog.service;

import com.example.catalog.Exceptions.MenuItemAlreadyExistsException;
import com.example.catalog.Exceptions.RestaurantNotFoundException;
import com.example.catalog.model.MenuItem;
import com.example.catalog.model.Restaurant;
import com.example.catalog.repository.MenuItemRepository;
import com.example.catalog.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MenuItemService {
    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;

    @Autowired
    public MenuItemService(MenuItemRepository menuItemRepository, RestaurantRepository restaurantRepository) {
        this.menuItemRepository = menuItemRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public void addMenuItem(String restaurantName, String restaurantAddress, String itemName, double price) {
        Restaurant restaurant = restaurantRepository.findByNameAndAddress(restaurantName, restaurantAddress)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant '" + restaurantName + "' not found"));

        if (menuItemRepository.findByNameAndRestaurantId(itemName, restaurant.getId()).isPresent()) {
            throw new MenuItemAlreadyExistsException("Menu item '" + itemName + "' already exists for restaurant '" + restaurantName + "'");
        }

        MenuItem menuItem = restaurant.addMenuItem(itemName, price);
        menuItemRepository.save(menuItem);
    }
}
