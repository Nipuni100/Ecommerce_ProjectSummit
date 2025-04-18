package com.projectsummit.Carts_service.ExceptionHandling;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String message) {
        super(message);
    }
}