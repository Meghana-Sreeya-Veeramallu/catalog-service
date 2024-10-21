package com.example.catalog.Exceptions;

public class RestaurantNameCannotBeNullOrEmptyException extends RuntimeException {
    public RestaurantNameCannotBeNullOrEmptyException(String message) {
        super(message);
    }
}
