package com.example.catalog.service;

import com.example.catalog.Exceptions.RestaurantAddressCannotBeNullOrEmptyException;
import com.example.catalog.Exceptions.RestaurantAlreadyExistsException;
import com.example.catalog.Exceptions.RestaurantNameCannotBeNullOrEmptyException;
import com.example.catalog.Exceptions.RestaurantNotFoundException;
import com.example.catalog.model.Restaurant;
import com.example.catalog.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RestaurantServiceTest {

    @InjectMocks
    private RestaurantService restaurantService;

    @Mock
    private RestaurantRepository restaurantRepository;

    RestaurantServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddRestaurantSuccessfully() {
        String name = "Burger King";
        String address = "Hyderabad";
        when(restaurantRepository.findByNameAndAddress(name, address)).thenReturn(Optional.empty());

        restaurantService.addRestaurant(name, address);

        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
    }

    @Test
    void testAddRestaurantWithNullName() {
        String name = null;
        String address = "Hyderabad";

        RestaurantNameCannotBeNullOrEmptyException exception = assertThrows(RestaurantNameCannotBeNullOrEmptyException.class, () ->
                restaurantService.addRestaurant(name, address));

        assertEquals("Restaurant name cannot be null or empty", exception.getMessage());
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }

    @Test
    void testAddRestaurantWithNullAddress() {
        String name = "Burger King";
        String address = null;

        RestaurantAddressCannotBeNullOrEmptyException exception = assertThrows(RestaurantAddressCannotBeNullOrEmptyException.class, () ->
            restaurantService.addRestaurant(name, address));

        assertEquals("Restaurant address cannot be null or empty", exception.getMessage());
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }

    @Test
    void testAddRestaurantAlreadyExists() {
        String name = "Burger King";
        String address = "Hyderabad";
        when(restaurantRepository.findByNameAndAddress(name, address)).thenReturn(Optional.of(new Restaurant(name, address)));

        RestaurantAlreadyExistsException exception = assertThrows(RestaurantAlreadyExistsException.class, () ->
            restaurantService.addRestaurant(name, address));

        assertEquals("Restaurant with name 'Burger King' and address 'Hyderabad' already exists", exception.getMessage());
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }

    @Test
    void testAddRestaurantSameNameAndDifferentAddress() {
        String firstRestaurantName = "Burger King";
        String firstRestaurantAddress = "Hyderabad";
        String secondRestaurantName = "Burger King";
        String secondRestaurantAddress = "Bengaluru";

        when(restaurantRepository.findByNameAndAddress(firstRestaurantName, firstRestaurantAddress)).thenReturn(Optional.empty());
        when(restaurantRepository.findByNameAndAddress(secondRestaurantName, secondRestaurantAddress)).thenReturn(Optional.empty());

        restaurantService.addRestaurant(firstRestaurantName, firstRestaurantAddress);
        restaurantService.addRestaurant(secondRestaurantName, secondRestaurantAddress);

        verify(restaurantRepository, times(2)).save(any(Restaurant.class));
    }

    @Test
    void testGetAllRestaurants() {
        Restaurant restaurant1 = new Restaurant("Burger King", "Hyderabad");
        Restaurant restaurant2 = new Restaurant("Pizza Hut", "Bengaluru");

        when(restaurantRepository.findAll()).thenReturn(Arrays.asList(restaurant1, restaurant2));

        List<Restaurant> result = restaurantService.getAllRestaurants();

        assertEquals(2, result.size());
        verify(restaurantRepository, times(1)).findAll();
    }

    @Test
    void testGetAllRestaurantsEmpty() {
        when(restaurantRepository.findAll()).thenReturn(Collections.emptyList());

        List<Restaurant> result = restaurantService.getAllRestaurants();

        assertEquals(0, result.size());
        verify(restaurantRepository, times(1)).findAll();
    }

    @Test
    void testGetRestaurantByIdFound() {
        Long restaurantId = 1L;
        Restaurant restaurant = new Restaurant("Burger King", "Hyderabad");

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));

        Restaurant result = restaurantService.getRestaurantById(restaurantId);

        assertEquals(restaurant, result);
        verify(restaurantRepository, times(1)).findById(restaurantId);
    }

    @Test
    void testGetRestaurantByIdNotFound() {
        Long restaurantId = 999L;

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        RestaurantNotFoundException exception = assertThrows(RestaurantNotFoundException.class, () ->
                restaurantService.getRestaurantById(restaurantId));

        assertEquals("Restaurant with id '999' not found", exception.getMessage());
        verify(restaurantRepository, times(1)).findById(restaurantId);
    }
}
