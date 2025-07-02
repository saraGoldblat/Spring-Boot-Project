package com.example.recipes.exception;

/**
 * Exception למשאב שלא נמצא
 */
public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s לא נמצא עם %s: %s", resourceName, fieldName, fieldValue));
    }
}
