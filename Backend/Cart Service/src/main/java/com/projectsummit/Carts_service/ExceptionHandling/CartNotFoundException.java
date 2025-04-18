package com.projectsummit.Carts_service.ExceptionHandling;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(String message) {
        super(message);
    }
}
