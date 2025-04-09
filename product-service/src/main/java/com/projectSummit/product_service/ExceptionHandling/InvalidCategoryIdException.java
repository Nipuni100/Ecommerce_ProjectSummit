package com.projectSummit.product_service.ExceptionHandling;

public class InvalidCategoryIdException extends RuntimeException {
    public InvalidCategoryIdException(String message) {
        super(message);
    }
}
