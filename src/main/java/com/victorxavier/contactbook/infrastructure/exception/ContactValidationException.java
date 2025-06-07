package com.victorxavier.contactbook.infrastructure.exception;

import java.util.List;

/**
 * Exception for contact validation errors.
 * Contains list of field-level validation errors.
 */
public class ContactValidationException extends RuntimeException {

    private final List<FieldMessage> fieldErrors;

    public ContactValidationException(List<FieldMessage> fieldErrors) {
        super("Contact validation failed");
        this.fieldErrors = fieldErrors;
    }

    public List<FieldMessage> getFieldErrors() {
        return fieldErrors;
    }
}