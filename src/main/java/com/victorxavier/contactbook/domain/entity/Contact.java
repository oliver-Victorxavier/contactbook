package com.victorxavier.contactbook.domain.entity;

/**
 * Domain entity representing a contact with address information
 */
public class Contact extends BaseAddressEntity {

    public Contact() {}
    
    public Contact(Long id, String name, String phone, String cep, Integer numero) {
        super(id, name, phone, cep, numero);
    }

    public Contact(String name, String phone, String cep, Integer numero) {
        this(null, name, phone, cep, numero);
    }
}