package com.projectSummit.product_service.ExceptionHandling;

public class InvalidProductIdException extends RuntimeException {
    public InvalidProductIdException(String message) {
        super(message);
    }
}
