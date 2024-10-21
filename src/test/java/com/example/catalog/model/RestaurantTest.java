package com.example.catalog.model;

import com.example.catalog.Exceptions.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantTest {

    @Test
    void testConstructorValidInput() {
        Restaurant restaurant = new Restaurant("Domino's", "Kukatpally, Hyderabad");
        
        assertNotNull(restaurant);
    }

    @Test
    void testConstructorNameNull() {
        Exception exception = assertThrows(RestaurantNameCannotBeNullOrEmptyException.class, () -> {
            new Restaurant(null, "Kukatpally, Hyderabad");
        });
        assertEquals("Restaurant name cannot be null or empty", exception.getMessage());
    }

    @Test
    void testConstructorNameEmpty() {
        Exception exception = assertThrows(RestaurantNameCannotBeNullOrEmptyException.class, () -> {
            new Restaurant("   ", "Kukatpally, Hyderabad");
        });
        assertEquals("Restaurant name cannot be null or empty", exception.getMessage());
    }

    @Test
    void testConstructorAddressNull() {
        Exception exception = assertThrows(RestaurantAddressCannotBeNullOrEmptyException.class, () -> {
            new Restaurant("Domino's", null);
        });
        assertEquals("Restaurant address cannot be null or empty", exception.getMessage());
    }

    @Test
    void testConstructorAddressEmpty() {
        Exception exception = assertThrows(RestaurantAddressCannotBeNullOrEmptyException.class, () -> {
            new Restaurant("Domino's", "  ");
        });
        assertEquals("Restaurant address cannot be null or empty", exception.getMessage());
    }

    @Test
    void testAddMenuItem() {
        Restaurant restaurant = new Restaurant("Domino's", "Kukatpally, Hyderabad");

        MenuItem menuItem = restaurant.addMenuItem("Margherita", 150);

        assertNotNull(menuItem);
    }
}
