package com.victorxavier.contactbook.application.service;

import com.victorxavier.contactbook.application.dto.request.ContactRequest;
import com.victorxavier.contactbook.application.dto.request.ContactUpdateRequest;
import com.victorxavier.contactbook.application.dto.response.ContactResponse;
import com.victorxavier.contactbook.application.dto.response.PageResponse;
import com.victorxavier.contactbook.application.mapper.ContactMapper;
import com.victorxavier.contactbook.domain.entity.Contact;
import com.victorxavier.contactbook.domain.repository.ContactRepository;
import com.victorxavier.contactbook.domain.service.AddressService;
import com.victorxavier.contactbook.infrastructure.exception.*;
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

import java.util.ArrayList;
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

        List<FieldMessage> validationErrors = validateContactRequest(request);
        if (!validationErrors.isEmpty()) {
            throw new ContactValidationException(validationErrors);
        }

        Contact contact = mapper.toEntity(request);

        try {
            AddressService.AddressInfo addressInfo = addressService.getAddressByCep(contact.getCep());
            contact.setAddress(
                    addressInfo.getLogradouro(),
                    addressInfo.getBairro(),
                    addressInfo.getCidade(),
                    addressInfo.getEstado()
            );
        } catch (AddressNotFoundException ex) {
            log.warn("Address not found for CEP {}: {}", contact.getCep(), ex.getMessage());
            throw ex;
        } catch (ExternalServiceException ex) {
            log.error("External service error when fetching CEP {}: {}", contact.getCep(), ex.getMessage());
            throw ex;
        } catch (IllegalArgumentException ex) {
            log.warn("Invalid CEP format for {}: {}", contact.getCep(), ex.getMessage());
            throw ex;
        }

        Contact savedContact = repository.save(contact);
        log.info("Contact saved successfully with ID: {}", savedContact.getId());
        return mapper.toResponse(savedContact);
    }

    private List<FieldMessage> validateContactRequest(ContactRequest request) {
        List<FieldMessage> errors = new ArrayList<>();

        if (request.getName() == null || request.getName().trim().isEmpty()) {
            errors.add(new FieldMessage("name", "Nome é obrigatório"));
        } else if (request.getName().trim().length() < 2) {
            errors.add(new FieldMessage("name", "Nome deve ter pelo menos 2 caracteres"));
        }

        if (request.getPhone() == null || request.getPhone().trim().isEmpty()) {
            errors.add(new FieldMessage("phone", "Telefone é obrigatório"));
        } else if (!isValidPhone(request.getPhone())) {
            errors.add(new FieldMessage("phone", "Formato de telefone inválido"));
        }

        if (request.getCep() == null || request.getCep().trim().isEmpty()) {
            errors.add(new FieldMessage("cep", "CEP é obrigatório"));
        } else if (!isValidCepFormat(request.getCep())) {
            errors.add(new FieldMessage("cep", "CEP deve conter exatamente 8 dígitos"));
        }

        if (request.getNumero() == null || request.getNumero() <= 0) {
            errors.add(new FieldMessage("numero", "Número deve ser maior que zero"));
        }
        return errors;
    }

    private boolean isValidPhone(String phone) {
        String cleanPhone = phone.replaceAll("[^0-9]", "");
        return cleanPhone.length() >= 10 && cleanPhone.length() <= 11;
    }

    private boolean isValidCepFormat(String cep) {
        String cleanCep = cep.replaceAll("[^0-9]", "");
        return cleanCep.length() == 8;
    }

    @Override
    public ContactResponse update(Long id, ContactUpdateRequest request) {
        log.info("Updating contact with ID: {}", id);
        Contact contact = repository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException("Contato não encontrado com ID: " + id));

        String oldCep = contact.getCep();
        mapper.updateEntity(contact, request);

        if (request.getCep() != null && !request.getCep().trim().isEmpty() && !request.getCep().equals(oldCep)) {
            try {
                AddressService.AddressInfo addressInfo = addressService.getAddressByCep(contact.getCep());
                contact.setAddress(
                        addressInfo.getLogradouro(),
                        addressInfo.getBairro(),
                        addressInfo.getCidade(),
                        addressInfo.getEstado()
                );
            } catch (AddressNotFoundException ex) {
                log.warn("Address not found for new CEP {} during update of contact ID {}: {}", contact.getCep(), id, ex.getMessage());
                throw ex;
            } catch (ExternalServiceException ex) {
                log.error("External service error for new CEP {} during update of contact ID {}: {}", contact.getCep(), id, ex.getMessage());
                throw ex;
            } catch (IllegalArgumentException ex) {
                log.warn("Invalid CEP format for {} during update of contact ID {}: {}", contact.getCep(), id, ex.getMessage());
                throw ex;
            }
        } else if (request.getCep() != null && request.getCep().trim().isEmpty()) {
            contact.clearAddress();
            log.info("CEP provided as empty for contact ID {}, clearing address fields.", id);
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
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ContactNotFoundException("Contato não encontrado com ID: " + id));
    }

    /**
     * Finds contacts by name, handling multiple results.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ContactResponse> findByName(String name) {
        log.info("Buscando contatos pelo nome: {}", name);

        List<Contact> contacts = repository.findByName(name);

        if (contacts.isEmpty()) {
            throw new ContactNotFoundException("Nenhum contato encontrado para o nome: '" + name + "'");
        }

        log.info("Encontrados {} contatos para o nome: {}", contacts.size(), name);
        return contacts.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ContactResponse> findAll(Pageable pageable) {
        log.info("Finding all contacts with pagination");
        Page<Contact> contactPage = repository.findAll(pageable);
        return new PageResponse<>(contactPage.map(mapper::toResponse));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ContactResponse> findBySearchTerm(String search, Pageable pageable) {
        log.info("Searching contacts with term: {}", search);
        Page<Contact> contactPage = repository.findBySearchTerm(search, pageable);
        return new PageResponse<>(contactPage.map(mapper::toResponse));
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