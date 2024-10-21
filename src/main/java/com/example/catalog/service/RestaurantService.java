package com.example.catalog.service;

import com.example.catalog.Exceptions.RestaurantAlreadyExistsException;
import com.example.catalog.model.Restaurant;
import com.example.catalog.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public void addRestaurant(String name, String address) {
        if (restaurantRepository.findByNameAndAddress(name, address).isPresent()) {
            throw new RestaurantAlreadyExistsException("Restaurant with name '" + name + "' and address '" + address + "' already exists");
        }

        Restaurant restaurant = new Restaurant(name, address);
        restaurantRepository.save(restaurant);
    }
}
