package com.victorxavier.contactbook.application.port.out;

import com.victorxavier.contactbook.domain.entity.Contact;
import java.util.List;

public interface ContactExportPort {
    byte[] export(List<Contact> contacts);
    String getMimeType();
    String getFilename();
    boolean canHandle(String format);
}