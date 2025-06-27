package com.victorxavier.contactbook.application.service;

import com.victorxavier.contactbook.application.dto.request.ContactRequest;
import com.victorxavier.contactbook.application.dto.request.ContactUpdateRequest;
import com.victorxavier.contactbook.application.dto.response.ContactResponse;
import com.victorxavier.contactbook.application.dto.response.ExportedFile;
import com.victorxavier.contactbook.application.dto.response.PageResponse;
import com.victorxavier.contactbook.application.mapper.ContactMapper;
import com.victorxavier.contactbook.application.port.in.CsvImportPort;
import com.victorxavier.contactbook.application.port.out.ContactExportPort;
import com.victorxavier.contactbook.domain.entity.Contact;
import com.victorxavier.contactbook.domain.repository.ContactRepository;
import com.victorxavier.contactbook.domain.service.AddressService;
import com.victorxavier.contactbook.infrastructure.exception.AddressNotFoundException;
import com.victorxavier.contactbook.infrastructure.exception.ContactNotFoundException;
import com.victorxavier.contactbook.infrastructure.exception.ExternalServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ContactServiceImpl implements ContactService {

    private static final Logger log = LoggerFactory.getLogger(ContactServiceImpl.class);

    private final ContactRepository contactRepository;
    private final AddressService addressService;
    private final ContactMapper mapper;
    private final CsvImportPort csvImportPort;
    private final List<ContactExportPort> exportPorts;

    public ContactServiceImpl(ContactRepository contactRepository,
                              AddressService addressService,
                              ContactMapper mapper,
                              CsvImportPort csvImportPort,
                              List<ContactExportPort> exportPorts) {
        this.contactRepository = contactRepository;
        this.addressService = addressService;
        this.mapper = mapper;
        this.csvImportPort = csvImportPort;
        this.exportPorts = exportPorts;
    }

    @Override
    public ContactResponse save(ContactRequest request) {
        log.info("Saving contact with name: {}", request.getName());
        Contact contact = mapper.toEntity(request);
        populateAddress(contact);
        Contact savedContact = contactRepository.save(contact);
        log.info("Contact saved successfully with ID: {}", savedContact.getId());
        return mapper.toResponse(savedContact);
    }

    @Override
    public ContactResponse update(Long id, ContactUpdateRequest request) {
        log.info("Updating contact with ID: {}", id);
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> ContactNotFoundException.byId(id));

        String oldCep = contact.getCep();
        mapper.updateEntity(contact, request);

        if (request.getCep() != null && !request.getCep().trim().isEmpty() && !request.getCep().equals(oldCep)) {
            populateAddress(contact);
        }

        Contact updatedContact = contactRepository.save(contact);
        log.info("Contact updated successfully with ID: {}", updatedContact.getId());
        return mapper.toResponse(updatedContact);
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting contact with ID: {}", id);
        if (!contactRepository.existsById(id)) {
            throw ContactNotFoundException.byId(id);
        }
        contactRepository.deleteById(id);
        log.info("Contact deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public ContactResponse findById(Long id) {
        log.info("Finding contact by ID: {}", id);
        return contactRepository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> ContactNotFoundException.byId(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactResponse> findByName(String name) {
        log.info("Buscando contatos pelo nome: {}", name);
        List<Contact> contacts = contactRepository.findByName(name);
        if (contacts.isEmpty()) {
            throw ContactNotFoundException.byName(name);
        }
        return contacts.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ContactResponse> findAll(Pageable pageable) {
        log.info("Finding all contacts with pagination");
        Page<Contact> contactPage = contactRepository.findAll(pageable);
        return new PageResponse<>(contactPage.map(mapper::toResponse));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ContactResponse> findBySearchTerm(String search, Pageable pageable) {
        log.info("Searching contacts with term: {}", search);
        Page<Contact> contactPage = contactRepository.findBySearchTerm(search, pageable);
        return new PageResponse<>(contactPage.map(mapper::toResponse));
    }

    @Override
    public List<ContactResponse> importFromCsv(MultipartFile file) {
        log.info("Importing contacts from CSV file: {}", file.getOriginalFilename());
        List<Contact> contacts = csvImportPort.importContacts(file);
        List<Contact> savedContacts = contactRepository.saveAll(contacts);
        log.info("Imported {} contacts successfully", savedContacts.size());
        return savedContacts.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ExportedFile export(String format) {
        log.info("Preparing export for format: {}", format);

        ContactExportPort exportPort = exportPorts.stream()
                .filter(port -> port.canHandle(format))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Formato de exportação inválido ou não suportado: " + format));

        List<Contact> contacts = contactRepository.findAll();
        byte[] data = exportPort.export(contacts);

        log.info("Export data generated successfully for format: {}. Filename: {}, ContentType: {}",
                format, exportPort.getFilename(), exportPort.getMimeType());

        return new ExportedFile(data, exportPort.getFilename(), exportPort.getMimeType());
    }
    private void populateAddress(Contact contact) {
        if (contact.getCep() == null || contact.getCep().isBlank()) {
            return;
        }
        try {
            AddressService.AddressInfo addressInfo = addressService.getAddressByCep(contact.getCep());
            contact.setAddress(
                    addressInfo.getLogradouro(),
                    addressInfo.getBairro(),
                    addressInfo.getCidade(),
                    addressInfo.getEstado()
            );
        } catch (AddressNotFoundException | ExternalServiceException | IllegalArgumentException ex) {
            log.warn("Could not populate address for CEP {}: {}", contact.getCep(), ex.getMessage());
            throw ex;
        }
    }
}
