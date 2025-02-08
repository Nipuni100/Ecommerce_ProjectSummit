package com.projectsummit.Order_service.ExceptionHandling;

public class OrderNotFoundException extends RuntimeException {

    // Constructor that accepts a message
    public OrderNotFoundException(String message) {
        super(message);
    }
}