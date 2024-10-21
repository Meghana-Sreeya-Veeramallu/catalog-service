package com.example.catalog.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RestaurantNameCannotBeNullOrEmptyException.class)
    public ResponseEntity<String> handleRestaurantNameCannotBeNullOrEmpty(RestaurantNameCannotBeNullOrEmptyException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request: " + ex.getMessage());
    }

    @ExceptionHandler(RestaurantAddressCannotBeNullOrEmptyException.class)
    public ResponseEntity<String> handleRestaurantAddressCannotBeNullOrEmpty(RestaurantAddressCannotBeNullOrEmptyException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request: " + ex.getMessage());
    }

    @ExceptionHandler(RestaurantAlreadyExistsException.class)
    public ResponseEntity<String> handleRestaurantAlreadyExists(RestaurantAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: " + ex.getMessage());
    }

    @ExceptionHandler(MenuItemNameCannotBeNullOrEmptyException.class)
    public ResponseEntity<String> handleMenuItemNameCannotBeNullOrEmpty(MenuItemNameCannotBeNullOrEmptyException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request: " + ex.getMessage());
    }

    @ExceptionHandler(PriceMustBePositiveException.class)
    public ResponseEntity<String> handlePriceMustBePositive(PriceMustBePositiveException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request: " + ex.getMessage());
    }

    @ExceptionHandler(RestaurantNotFoundException.class)
    public ResponseEntity<String> handleRestaurantNotFound(RestaurantNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found: " + ex.getMessage());
    }

    @ExceptionHandler(MenuItemAlreadyExistsException.class)
    public ResponseEntity<String> handleMenuItemAlreadyExists(MenuItemAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception e) {
        return ResponseEntity.internalServerError().body("An error occurred: " + e.getMessage());
    }
}
