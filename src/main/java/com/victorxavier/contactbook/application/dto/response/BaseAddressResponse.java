package com.victorxavier.contactbook.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Base class for contact response DTOs with address fields
 */
public abstract class BaseAddressResponse extends BaseContactResponse implements AddressResponse {

    public BaseAddressResponse() {}
    
    public BaseAddressResponse(Long id, String name, String phone, String cep,
                           String logradouro, Integer numero, String bairro,
                           String cidade, String estado) {
        super(id, name, phone, cep, logradouro, numero, bairro, cidade, estado);
    }
    
    /**
     * Formats the complete address as a single string
     */
    @Override
    protected String buildEnderecoCompleto() {
        if (getLogradouro() == null || getCidade() == null || getEstado() == null) {
            return null;
        }
        return String.format("%s, %d - %s, %s - %s",
                getLogradouro(), getNumero() != null ? getNumero() : 0,
                getBairro() != null ? getBairro() : "", getCidade(), getEstado());
    }
}