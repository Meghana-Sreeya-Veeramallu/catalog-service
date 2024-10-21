package com.example.catalog.controller;

import com.example.catalog.Dto.MenuItemDto;
import com.example.catalog.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
