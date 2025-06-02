package com.victorxavier.contactbook.infrastructure.service;


import com.victorxavier.contactbook.domain.entity.Contact;
import com.victorxavier.contactbook.domain.service.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvImportService {

    private static final Logger log = LoggerFactory.getLogger(CsvImportService.class);

    private final AddressService addressService;

    public CsvImportService(AddressService addressService) {
        this.addressService = addressService;
    }

    public List<Contact> importContacts(MultipartFile file) {
        List<Contact> contacts = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header
                }

                String[] data = line.split(",");
                if (data.length >= 4) {
                    Contact contact = createContactFromCsv(data);
                    if (contact != null) {
                        contacts.add(contact);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error importing CSV file", e);
            throw new IllegalArgumentException("Erro ao importar arquivo CSV: " + e.getMessage());
        }

        return contacts;
    }

    private Contact createContactFromCsv(String[] data) {
        try {
            String name = data[0].trim();
            String phone = data[1].trim();
            String cep = data[2].trim().replace("-", "");
            Integer numero = Integer.parseInt(data[3].trim());

            Contact contact = new Contact(name, phone, cep, numero);

            // Buscar endereço
            try {
                AddressService.AddressInfo addressInfo = addressService.getAddressByCep(cep);
                contact.setAddress(
                        addressInfo.getLogradouro(),
                        addressInfo.getBairro(),
                        addressInfo.getCidade(),
                        addressInfo.getEstado()
                );
            } catch (Exception ex) {
                log.warn("Could not find address for CEP: {} in CSV import", cep);
            }

            return contact;
        } catch (Exception e) {
            log.warn("Error parsing CSV line: {}", String.join(",", data));
            return null;
        }
    }
}