package com.example.catalog.service;

import com.example.catalog.Exceptions.RestaurantAddressCannotBeNullOrEmptyException;
import com.example.catalog.Exceptions.RestaurantAlreadyExistsException;
import com.example.catalog.Exceptions.RestaurantNameCannotBeNullOrEmptyException;
import com.example.catalog.model.Restaurant;
import com.example.catalog.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
}
