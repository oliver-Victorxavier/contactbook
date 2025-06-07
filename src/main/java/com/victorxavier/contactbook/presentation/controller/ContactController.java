package com.victorxavier.contactbook.presentation.controller;

import com.victorxavier.contactbook.application.dto.request.ContactRequest;
import com.victorxavier.contactbook.application.dto.request.ContactUpdateRequest;
import com.victorxavier.contactbook.application.dto.response.ContactResponse;
import com.victorxavier.contactbook.application.dto.response.PageResponse;
import com.victorxavier.contactbook.application.service.ContactService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    private static final Logger log = LoggerFactory.getLogger(ContactController.class);

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    public ResponseEntity<ContactResponse> createContact(@Valid @RequestBody ContactRequest request) {
        log.info("Creating contact with name: {}", request.getName());
        ContactResponse response = contactService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactResponse> updateContact(@PathVariable Long id,
                                                         @Valid @RequestBody ContactUpdateRequest request) {
        log.info("Updating contact with ID: {}", id);
        ContactResponse response = contactService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        log.info("Deleting contact with ID: {}", id);
        contactService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactResponse> getContactById(@PathVariable Long id) {
        log.info("Getting contact by ID: {}", id);
        ContactResponse response = contactService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ContactResponse> getContactByName(@PathVariable String name) {
        log.info("Getting contact by name: {}", name);
        ContactResponse response = contactService.findByName(name);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<PageResponse<ContactResponse>> getAllContacts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search) {

        log.info("Getting contacts - page: {}, size: {}, sortBy: {}, sortDir: {}, search: {}",
                page, size, sortBy, sortDir, search);

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        PageResponse<ContactResponse> response = search != null && !search.trim().isEmpty()
                ? contactService.findBySearchTerm(search, pageable)
                : contactService.findAll(pageable);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/import")
    public ResponseEntity<List<ContactResponse>> importContacts(@RequestParam("file") MultipartFile file) {
        log.info("Importing contacts from file: {}", file.getOriginalFilename());

        if (file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo não pode estar vazio");
        }

        if (!file.getOriginalFilename().toLowerCase().endsWith(".csv")) {
            throw new IllegalArgumentException("Apenas arquivos CSV são suportados");
        }

        List<ContactResponse> response = contactService.importFromCsv(file);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/export/excel")
    public ResponseEntity<byte[]> exportToExcel() {
        log.info("Exporting contacts to Excel");

        byte[] excelData = contactService.exportToExcel();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''contatos.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelData);
    }

    @GetMapping("/export/pdf")
    public ResponseEntity<byte[]> exportToPdf() {
        log.info("Exporting contacts to PDF");

        byte[] pdfData = contactService.exportToPdf();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''contatos.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfData);
    }
}

