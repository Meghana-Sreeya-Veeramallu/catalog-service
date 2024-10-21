package com.example.catalog.model;

import com.example.catalog.Exceptions.MenuItemNameCannotBeNullOrEmptyException;
import com.example.catalog.Exceptions.PriceMustBePositiveException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MenuItemTest {

    @Test
    void testConstructorValidInput() {
        MenuItem menuItem = new MenuItem("Pasta", 199);

        assertNotNull(menuItem);
    }

    @Test
    void testConstructorNameNull() {
        Exception exception = assertThrows(MenuItemNameCannotBeNullOrEmptyException.class, () -> {
            new MenuItem(null, 199);
        });
        assertEquals("Menu item name cannot be null or empty", exception.getMessage());
    }

    @Test
    void testConstructorNameEmpty() {
        Exception exception = assertThrows(MenuItemNameCannotBeNullOrEmptyException.class, () -> {
            new MenuItem("   ", 199);
        });
        assertEquals("Menu item name cannot be null or empty", exception.getMessage());
    }

    @Test
    void testConstructorPriceNegative() {
        Exception exception = assertThrows(PriceMustBePositiveException.class, () -> {
            new MenuItem("Pasta", -50);
        });
        assertEquals("Price must be positive", exception.getMessage());
    }

    @Test
    void testConstructorPriceZero() {
        Exception exception = assertThrows(PriceMustBePositiveException.class, () -> {
            new MenuItem("Pasta", 0);
        });
        assertEquals("Price must be positive", exception.getMessage());
    }
}
