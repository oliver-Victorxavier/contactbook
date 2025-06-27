package com.victorxavier.contactbook.application.port.in;

import com.victorxavier.contactbook.domain.entity.Contact;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface CsvImportPort {
    List<Contact> importContacts(MultipartFile file);
}