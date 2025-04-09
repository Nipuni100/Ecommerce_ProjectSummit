package com.projectSummit.product_service.ExceptionHandling;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
