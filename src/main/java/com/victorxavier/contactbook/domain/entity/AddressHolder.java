package com.victorxavier.contactbook.domain.entity;

/**
 * Interface for entities that hold address information
 */
public interface AddressHolder {
    
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
     * Sets all address fields at once
     */
    void setAddress(String logradouro, String bairro, String cidade, String estado);
    
    /**
     * Clears all address fields
     */
    void clearAddress();
}