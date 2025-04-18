package com.projectsummit.Carts_service.ExceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // Optional: Maps this exception to a 404 HTTP status code
public class ResourceNotFoundException extends RuntimeException {

    // Constructor with a custom message
    public ResourceNotFoundException(String message) {
        super(message);
    }

    // Optional: Constructor with both message and cause
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
