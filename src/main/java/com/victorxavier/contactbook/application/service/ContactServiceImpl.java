package com.victorxavier.contactbook.application.service;

import com.victorxavier.contactbook.application.dto.request.ContactRequest;
import com.victorxavier.contactbook.application.dto.request.ContactUpdateRequest;
import com.victorxavier.contactbook.application.dto.response.ContactResponse;
import com.victorxavier.contactbook.application.dto.response.PageResponse;
import com.victorxavier.contactbook.application.mapper.ContactMapper;
import com.victorxavier.contactbook.domain.entity.Contact;
import com.victorxavier.contactbook.domain.repository.ContactRepository;
import com.victorxavier.contactbook.domain.service.AddressService;
import com.victorxavier.contactbook.infrastructure.exception.ContactNotFoundException;
import com.victorxavier.contactbook.infrastructure.service.CsvImportService;
import com.victorxavier.contactbook.infrastructure.service.ExcelExportService;
import com.victorxavier.contactbook.infrastructure.service.PdfExportService;
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

    private final ContactRepository repository;
    private final AddressService addressService;
    private final ContactMapper mapper;
    private final ExcelExportService excelExportService;
    private final PdfExportService pdfExportService;
    private final CsvImportService csvImportService;

    public ContactServiceImpl(ContactRepository repository,
                              AddressService addressService,
                              ContactMapper mapper,
                              ExcelExportService excelExportService,
                              PdfExportService pdfExportService,
                              CsvImportService csvImportService) {
        this.repository = repository;
        this.addressService = addressService;
        this.mapper = mapper;
        this.excelExportService = excelExportService;
        this.pdfExportService = pdfExportService;
        this.csvImportService = csvImportService;
    }

    @Override
    public ContactResponse save(ContactRequest request) {
        log.info("Saving contact with name: {}", request.getName());

        Contact contact = mapper.toEntity(request);

        try {
            AddressService.AddressInfo addressInfo = addressService.getAddressByCep(contact.getCep());
            contact.setAddress(
                    addressInfo.getLogradouro(),
                    addressInfo.getBairro(),
                    addressInfo.getCidade(),
                    addressInfo.getEstado()
            );
        } catch (Exception ex) {
            log.warn("Could not find address for CEP: {}, error: {}", contact.getCep(), ex.getMessage());
            throw new IllegalArgumentException("CEP não encontrado: " + contact.getCep());
        }

        Contact savedContact = repository.save(contact);
        log.info("Contact saved successfully with ID: {}", savedContact.getId());

        return mapper.toResponse(savedContact);
    }

    @Override
    public ContactResponse update(Long id, ContactUpdateRequest request) {
        log.info("Updating contact with ID: {}", id);

        Contact contact = repository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException("Contato não encontrado com ID: " + id));

        String oldCep = contact.getCep();
        mapper.updateEntity(contact, request);

        // Se o CEP foi alterado, buscar novo endereço
        if (request.getCep() != null && !contact.getCep().equals(oldCep)) {
            try {
                AddressService.AddressInfo addressInfo = addressService.getAddressByCep(contact.getCep());
                contact.setAddress(
                        addressInfo.getLogradouro(),
                        addressInfo.getBairro(),
                        addressInfo.getCidade(),
                        addressInfo.getEstado()
                );
            } catch (Exception ex) {
                log.warn("Could not find address for new CEP: {}", contact.getCep());
                throw new IllegalArgumentException("CEP não encontrado: " + contact.getCep());
            }
        }

        Contact updatedContact = repository.save(contact);
        log.info("Contact updated successfully with ID: {}", updatedContact.getId());

        return mapper.toResponse(updatedContact);
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting contact with ID: {}", id);

        if (!repository.existsById(id)) {
            throw new ContactNotFoundException("Contato não encontrado com ID: " + id);
        }

        repository.deleteById(id);
        log.info("Contact deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public ContactResponse findById(Long id) {
        log.info("Finding contact by ID: {}", id);

        Contact contact = repository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException("Contato não encontrado com ID: " + id));

        return mapper.toResponse(contact);
    }

    @Override
    @Transactional(readOnly = true)
    public ContactResponse findByName(String name) {
        log.info("Finding contact by name: {}", name);

        Contact contact = repository.findByName(name)
                .orElseThrow(() -> new ContactNotFoundException("Contato não encontrado com nome: " + name));

        return mapper.toResponse(contact);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ContactResponse> findAll(Pageable pageable) {
        log.info("Finding all contacts with pagination");

        Page<Contact> contactPage = repository.findAll(pageable);
        Page<ContactResponse> responsePage = contactPage.map(mapper::toResponse);

        return new PageResponse<>(responsePage);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ContactResponse> findBySearchTerm(String search, Pageable pageable) {
        log.info("Searching contacts with term: {}", search);

        Page<Contact> contactPage = repository.findBySearchTerm(search, pageable);
        Page<ContactResponse> responsePage = contactPage.map(mapper::toResponse);

        return new PageResponse<>(responsePage);
    }

    @Override
    public List<ContactResponse> importFromCsv(MultipartFile file) {
        log.info("Importing contacts from CSV file: {}", file.getOriginalFilename());

        List<Contact> contacts = csvImportService.importContacts(file);
        List<Contact> savedContacts = repository.saveAll(contacts);

        log.info("Imported {} contacts successfully", savedContacts.size());

        return savedContacts.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] exportToExcel() {
        log.info("Exporting contacts to Excel");

        List<Contact> contacts = repository.findAll();
        return excelExportService.exportContacts(contacts);
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] exportToPdf() {
        log.info("Exporting contacts to PDF");

        List<Contact> contacts = repository.findAll();
        return pdfExportService.exportContacts(contacts);
    }
}