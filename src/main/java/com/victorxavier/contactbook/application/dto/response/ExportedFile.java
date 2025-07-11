package com.victorxavier.contactbook.application.dto.response;

public class ExportedFile {

    private final byte[] data;
    private final String filename;
    private final String contentType;

    public ExportedFile(byte[] data, String filename, String contentType) {
        this.data = data;
        this.filename = filename;
        this.contentType = contentType;
    }

    public byte[] getData() {
        return data;
    }

    public String getFilename() {
        return filename;
    }

    public String getContentType() {
        return contentType;
    }
}