package com.projectSummit.product_service.ExceptionHandling;

public class CategoryIDNotFoundException extends RuntimeException {
    public CategoryIDNotFoundException(String message) {
        super(message);
    }
}
