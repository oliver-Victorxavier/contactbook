package com.victorxavier.contactbook.infrastructure.service;

import com.victorxavier.contactbook.application.port.in.CsvImportPort;
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
import java.util.Optional;

@Service
public class CsvImportService implements CsvImportPort {

    private static final Logger log = LoggerFactory.getLogger(CsvImportService.class);
    private static final int MINIMUM_COLUMNS = 4;

    private final AddressService addressService;

    public CsvImportService(AddressService addressService) {
        this.addressService = addressService;
    }

    @Override
    public List<Contact> importContacts(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        List<Contact> contacts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            int lineNumber = 0;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] fields = line.split(",");
                if (fields.length < MINIMUM_COLUMNS) {
                    log.warn("Skipping malformed line {}: {}", lineNumber, line);
                    continue;
                }

                createContactFromCsv(fields)
                    .ifPresent(contacts::add);
            }
        } catch (Exception e) {
            log.error("Error processing CSV file", e);
            throw new RuntimeException("Failed to process CSV file: " + e.getMessage());
        }

        log.info("Successfully parsed {} contacts from CSV file.", contacts.size());
        return contacts;
    }

    private Optional<Contact> createContactFromCsv(String[] data) {
        try {
            String name = data[0].trim();
            String phone = data[1].trim();
            String cep = data[2].trim().replace("-", "");
            Integer numero = Integer.parseInt(data[3].trim());

            Contact contact = new Contact(name, phone, cep, numero);

            try {
                AddressService.AddressInfo addressInfo = addressService.getAddressByCep(cep);
                contact.setAddress(
                        addressInfo.getLogradouro(),
                        addressInfo.getBairro(),
                        addressInfo.getCidade(),
                        addressInfo.getEstado()
                );
            } catch (Exception ex) {
                log.warn("Could not find address for CEP: {} during CSV import. Contact will be imported without full address.", cep);
            }

            return Optional.of(contact);
        } catch (NumberFormatException e) {
            log.warn("Error parsing number in CSV line: {}. Skipping line.", String.join(",", data));
            return Optional.empty();
        } catch (Exception e) {
            log.warn("Error parsing generic data in CSV line: {}. Skipping line.", String.join(",", data));
            return Optional.empty();
        }
    }
}