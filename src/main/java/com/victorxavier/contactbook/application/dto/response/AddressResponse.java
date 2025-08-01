package com.victorxavier.contactbook.application.dto.response;

/**
 * Interface for response DTOs that include address information
 */
public interface AddressResponse {
    
    /**
     * Gets the CEP (Brazilian postal code)
     */
    String getCep();
    
    /**
     * Gets the street name
     */
    String getLogradouro();
    
    /**
     * Gets the address number
     */
    Integer getNumero();
    
    /**
     * Gets the neighborhood
     */
    String getBairro();
    
    /**
     * Gets the city
     */
    String getCidade();
    
    /**
     * Gets the state
     */
    String getEstado();
    
    /**
     * Gets the complete formatted address
     */
    String getEnderecoCompleto();
}