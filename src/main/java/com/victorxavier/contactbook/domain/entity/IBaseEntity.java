package com.victorxavier.contactbook.domain.entity;

/**
 * Interface for base entity methods
 */
public interface IBaseEntity {
    
    /**
     * Gets the entity ID
     */
    Long getId();
    
    /**
     * Sets the entity ID
     */
    void setId(Long id);
    
    /**
     * Gets the entity name
     */
    String getName();
    
    /**
     * Sets the entity name
     */
    void setName(String name);
}