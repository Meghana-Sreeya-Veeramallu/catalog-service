package com.example.catalog.controller;

import com.example.catalog.Dto.RestaurantDto;
import com.example.catalog.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/catalog/restaurants")
public class RestaurantController {
    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping
    public ResponseEntity<String> addRestaurant(@RequestBody RestaurantDto restaurantDto) {
        restaurantService.addRestaurant(restaurantDto.getName(), restaurantDto.getAddress());
        String successMessage = "Restaurant added successfully: " + restaurantDto.getName() + " at address: " + restaurantDto.getAddress();
        return ResponseEntity.ok(successMessage);
    }
}
