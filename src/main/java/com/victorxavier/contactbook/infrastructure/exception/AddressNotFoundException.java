package com.victorxavier.contactbook.infrastructure.exception;

/**
 * Exception thrown when an address is not found for a given CEP.
 *
 * @author Victor Xavier
 * @since 1.0
 */
public class AddressNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String cep;

    public AddressNotFoundException(String message) {
        super(message);
        this.cep = null;
    }

    public AddressNotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.cep = null;
    }

    /**
     * Enhanced constructor with CEP for better debugging
     */
    public AddressNotFoundException(String message, String cep) {
        super(message);
        this.cep = cep;
    }

    public AddressNotFoundException(String message, String cep, Throwable cause) {
        super(message, cause);
        this.cep = cep;
    }

    public String getCep() {
        return cep;
    }

    /**
     * Factory method for CEP not found scenarios
     */
    public static AddressNotFoundException forCep(String cep) {
        return new AddressNotFoundException(
                String.format("CEP não encontrado: %s", cep),
                cep
        );
    }
}