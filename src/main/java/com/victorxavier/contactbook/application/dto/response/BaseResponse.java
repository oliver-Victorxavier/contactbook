package com.victorxavier.contactbook.application.dto.response;

/**
 * Base class for all response DTOs
 */
public abstract class BaseResponse {
    
    /**
     * Converts the response to a JSON string
     * @return JSON representation of the response
     */
    public String toJson() {
        // This is a placeholder implementation
        // In a real application, you would use a JSON serialization library
        return "{}";
    }
    
    /**
     * Converts the response to an XML string
     * @return XML representation of the response
     */
    public String toXml() {
        // This is a placeholder implementation
        // In a real application, you would use an XML serialization library
        return "<response></response>";
    }
}