package com.example.catalog.Exceptions;

public class MenuItemAlreadyExistsException extends RuntimeException {
    public MenuItemAlreadyExistsException(String message) {
        super(message);
    }
}
