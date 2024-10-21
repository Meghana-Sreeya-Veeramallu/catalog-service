package com.example.catalog.service;

import com.example.catalog.Exceptions.MenuItemAlreadyExistsException;
import com.example.catalog.Exceptions.MenuItemNameCannotBeNullOrEmptyException;
import com.example.catalog.Exceptions.PriceMustBePositiveException;
import com.example.catalog.Exceptions.RestaurantNotFoundException;
import com.example.catalog.model.MenuItem;
import com.example.catalog.model.Restaurant;
import com.example.catalog.repository.MenuItemRepository;
import com.example.catalog.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MenuItemServiceTest {

    @InjectMocks
    private MenuItemService menuItemService;

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    private Restaurant restaurant;

    @BeforeEach
     void setUp() {
        MockitoAnnotations.openMocks(this);
        restaurant = new Restaurant("Test Restaurant", "Test address");
    }

    @Test
     void testAddMenuItem() {
        when(restaurantRepository.findByNameAndAddress("Test Restaurant", "Test Address"))
                .thenReturn(Optional.of(restaurant));
        when(menuItemRepository.findByNameAndRestaurantId("Pasta", restaurant.getId()))
                .thenReturn(Optional.empty());

        menuItemService.addMenuItem("Test Restaurant", "Test Address", "Pasta", 199);

        verify(menuItemRepository, times(1)).save(any(MenuItem.class));
    }

    @Test
    void testAddMenuItemWithNullName() {
        String itemName = null;
        int price = 199;

        when(restaurantRepository.findByNameAndAddress("Test Restaurant", "Test Address"))
                .thenReturn(Optional.of(restaurant));

        MenuItemNameCannotBeNullOrEmptyException exception = assertThrows(MenuItemNameCannotBeNullOrEmptyException.class, () ->
            menuItemService.addMenuItem("Test Restaurant", "Test Address", itemName, price));

        assertEquals("Menu item name cannot be null or empty", exception.getMessage());
        verify(menuItemRepository, never()).save(any(MenuItem.class));
    }

    @Test
    void testAddMenuItemWithZeroPrice() {
        String itemName = "Pasta";
        int price = 0;

        when(restaurantRepository.findByNameAndAddress("Test Restaurant", "Test Address"))
                .thenReturn(Optional.of(restaurant));
        when(menuItemRepository.findByNameAndRestaurantId(itemName, restaurant.getId()))
                .thenReturn(Optional.empty());

        PriceMustBePositiveException exception = assertThrows(PriceMustBePositiveException.class, () ->
            menuItemService.addMenuItem("Test Restaurant", "Test Address", itemName, price));

        assertEquals("Price must be positive", exception.getMessage());
        verify(menuItemRepository, never()).save(any(MenuItem.class));
    }

    @Test
    void testAddMenuItemWithNegativePrice() {
        String itemName = "Pasta";
        int price = -10;

        when(restaurantRepository.findByNameAndAddress("Test Restaurant", "Test Address"))
                .thenReturn(Optional.of(restaurant));
        when(menuItemRepository.findByNameAndRestaurantId(itemName, restaurant.getId()))
                .thenReturn(Optional.empty());

        PriceMustBePositiveException exception = assertThrows(PriceMustBePositiveException.class, () ->
            menuItemService.addMenuItem("Test Restaurant", "Test Address", itemName, price));

        assertEquals("Price must be positive", exception.getMessage());
        verify(menuItemRepository, never()).save(any(MenuItem.class));
    }


    @Test
     void testAddMenuItemRestaurantNotFound() {
        when(restaurantRepository.findByNameAndAddress("Invalid Restaurant", "Test Address"))
                .thenReturn(Optional.empty());

        RestaurantNotFoundException exception = assertThrows(RestaurantNotFoundException.class, () ->
            menuItemService.addMenuItem("Invalid Restaurant", "Test Address", "Pasta", 199));

        assertThat(exception.getMessage()).isEqualTo("Restaurant 'Invalid Restaurant' not found");
        verify(menuItemRepository, times(0)).save(any(MenuItem.class));
    }

    @Test
    public void testAddMenuItemAlreadyExists() {
        when(restaurantRepository.findByNameAndAddress("Test Restaurant", "Test Address"))
                .thenReturn(Optional.of(restaurant));
        when(menuItemRepository.findByNameAndRestaurantId("Pasta", restaurant.getId()))
                .thenReturn(Optional.of(new MenuItem("Pasta", 199)));

        MenuItemAlreadyExistsException exception = assertThrows(MenuItemAlreadyExistsException.class, () ->
            menuItemService.addMenuItem("Test Restaurant", "Test Address", "Pasta", 199));

        assertThat(exception.getMessage()).isEqualTo("Menu item 'Pasta' already exists for restaurant 'Test Restaurant'");
        verify(menuItemRepository, times(0)).save(any(MenuItem.class));
    }
}
