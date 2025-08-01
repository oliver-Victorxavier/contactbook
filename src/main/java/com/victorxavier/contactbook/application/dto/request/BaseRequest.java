package com.victorxavier.contactbook.application.dto.request;

/**
 * Base class for all request DTOs
 */
public abstract class BaseRequest {
    
    /**
     * Validates the request data
     * @return true if the request is valid, false otherwise
     */
    public boolean isValid() {
        return true; // Default implementation returns true, override in subclasses
    }
    
    /**
     * Gets validation error messages
     * @return array of error messages or empty array if no errors
     */
    public String[] getValidationErrors() {
        return new String[0]; // Default implementation returns empty array, override in subclasses
    }
}