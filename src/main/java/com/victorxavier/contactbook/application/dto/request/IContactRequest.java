package com.victorxavier.contactbook.application.dto.request;

/**
 * Interface for contact request DTOs
 */
public interface IContactRequest {
    
    /**
     * Gets the contact name
     */
    String getName();
    
    /**
     * Gets the contact phone number
     */
    String getPhone();
    
    /**
     * Gets the CEP (Brazilian postal code)
     */
    String getCep();
    
    /**
     * Gets the address number
     */
    Integer getNumero();
    
    /**
     * Gets the CEP without special characters
     */
    String getCleanCep();
}