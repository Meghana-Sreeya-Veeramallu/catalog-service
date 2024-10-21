package com.example.catalog.controller;

import com.example.catalog.Dto.MenuItemDto;
import com.example.catalog.model.MenuItem;
import com.example.catalog.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/catalog/restaurants/{restaurantId}/menuItems")
public class MenuItemController {
    private final MenuItemService menuItemService;

    @Autowired
    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @PostMapping
    public ResponseEntity<String> addMenuItem(
            @PathVariable Long restaurantId,
            @RequestBody MenuItemDto menuItemDto) {

        menuItemService.addMenuItem(restaurantId, menuItemDto.getName(), menuItemDto.getPrice());
        String successMessage = "Menu item added successfully: " + menuItemDto.getName() + " with price: " + menuItemDto.getPrice();
        return ResponseEntity.ok(successMessage);
    }

    @GetMapping
    public ResponseEntity<List<MenuItem>> getAllMenuItems(@PathVariable Long restaurantId) {
        List<MenuItem> menuItems = menuItemService.getAllMenuItems(restaurantId);
        return ResponseEntity.ok(menuItems);
    }

    @GetMapping("/{menuItemId}")
    public ResponseEntity<MenuItem> getMenuItemById(@PathVariable Long restaurantId, @PathVariable Long menuItemId) {
        MenuItem menuItem = menuItemService.getMenuItemById(menuItemId, restaurantId);
        return ResponseEntity.ok(menuItem);
    }
}
