package com.victorxavier.contactbook.application.port.out;

import com.victorxavier.contactbook.domain.entity.Contact;

import java.util.List;

/**
 * Abstract base class for contact export implementations
 * Provides common functionality for all export formats
 */
public abstract class AbstractContactExportPort implements ContactExportPort {

    private final String format;
    private final String mimeType;
    private final String fileExtension;

    protected AbstractContactExportPort(String format, String mimeType, String fileExtension) {
        this.format = format;
        this.mimeType = mimeType;
        this.fileExtension = fileExtension;
    }

    @Override
    public String getMimeType() {
        return mimeType;
    }

    @Override
    public String getFilename() {
        return "contacts." + fileExtension;
    }

    @Override
    public boolean canHandle(String format) {
        return this.format.equalsIgnoreCase(format);
    }

    /**
     * Template method to be implemented by concrete export classes
     */
    @Override
    public abstract byte[] export(List<Contact> contacts);
}