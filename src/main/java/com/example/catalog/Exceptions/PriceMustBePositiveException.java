package com.example.catalog.Exceptions;

public class PriceMustBePositiveException extends RuntimeException {
    public PriceMustBePositiveException(String message) {
        super(message);
    }
}
