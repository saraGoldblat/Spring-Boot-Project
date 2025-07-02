package com.example.recipes.exception;

/**
 * Exception לנתונים קיימים (למשל, username או email כפולים)
 */
public class DuplicateResourceException extends RuntimeException {
    
    public DuplicateResourceException(String message) {
        super(message);
    }
    
    public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s עם %s '%s' כבר קיים במערכת", resourceName, fieldName, fieldValue));
    }
}
