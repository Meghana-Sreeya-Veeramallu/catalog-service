package com.example.catalog.Exceptions;

public class RestaurantAddressCannotBeNullOrEmptyException extends RuntimeException {
    public RestaurantAddressCannotBeNullOrEmptyException(String message) {
        super(message);
    }
}
