package com.example.catalog.Exceptions;

public class MenuItemNameCannotBeNullOrEmptyException extends RuntimeException {
    public MenuItemNameCannotBeNullOrEmptyException(String message) {
        super(message);
    }
}
