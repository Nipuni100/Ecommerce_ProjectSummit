package com.projectSummit.product_service.ExceptionHandling;

public class EmptySearchQueryException extends RuntimeException {
    public EmptySearchQueryException(String message) {
        super(message);
    }
}
