package com.victorxavier.contactbook.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response DTO for contact information including address details
 */
@Schema(description = "Represents a contact as returned by the API.")
public class ContactResponse extends BaseAddressResponse {

    public ContactResponse() {}
    
    public ContactResponse(Long id, String name, String phone, String cep,
                           String logradouro, Integer numero, String bairro,
                           String cidade, String estado) {
        super(id, name, phone, cep, logradouro, numero, bairro, cidade, estado);
    }

}