package com.victorxavier.contactbook.infrastructure.exception;

/**
 * Exception thrown when a contact is not found in the system.
 *
 * @author Victor Xavier
 * @since 1.0
 */
public class ContactNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String contactIdentifier;

    public ContactNotFoundException(String message) {
        super(message);
        this.contactIdentifier = null;
    }

    public ContactNotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.contactIdentifier = null;
    }

    /**
     * Enhanced constructor with contact identifier for better debugging
     */
    public ContactNotFoundException(String message, String contactIdentifier) {
        super(message);
        this.contactIdentifier = contactIdentifier;
    }

    public ContactNotFoundException(String message, String contactIdentifier, Throwable cause) {
        super(message, cause);
        this.contactIdentifier = contactIdentifier;
    }

    public String getContactIdentifier() {
        return contactIdentifier;
    }

    /**
     * Factory methods for common scenarios
     */
    public static ContactNotFoundException byId(Long id) {
        return new ContactNotFoundException(
                String.format("Contato não encontrado com ID: %d", id),
                String.valueOf(id)
        );
    }

    public static ContactNotFoundException byName(String name) {
        return new ContactNotFoundException(
                String.format("Contato não encontrado com nome: %s", name),
                name
        );
    }
}