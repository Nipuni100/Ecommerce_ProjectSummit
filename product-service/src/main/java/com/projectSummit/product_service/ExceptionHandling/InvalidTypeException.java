package com.projectSummit.product_service.ExceptionHandling;

public class InvalidTypeException extends RuntimeException {
    public InvalidTypeException(String message) {
        super(message);
    }
}
