package com.victorxavier.contactbook.presentation.controller;

import com.victorxavier.contactbook.application.dto.request.ContactRequest;
import com.victorxavier.contactbook.application.dto.request.ContactUpdateRequest;
import com.victorxavier.contactbook.application.dto.response.ContactResponse;
import com.victorxavier.contactbook.application.dto.response.PageResponse;
import com.victorxavier.contactbook.application.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Contact Management", description = "Operations for managing contacts with automatic address resolution")
public class ContactController {

    private static final Logger log = LoggerFactory.getLogger(ContactController.class);
    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    @Operation(summary = "Create new contact", description = "Creates a new contact with automatic address resolution via CEP")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Contact created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
            @ApiResponse(responseCode = "404", description = "CEP not found", content = @Content),
            @ApiResponse(responseCode = "502", description = "Address service unavailable", content = @Content)
    })
    public ResponseEntity<ContactResponse> createContact(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Contact information to create",
                    content = @Content(schema = @Schema(implementation = ContactRequest.class))
            )
            @Valid @RequestBody ContactRequest request) {
        log.info("Creating contact with name: {}", request.getName());
        ContactResponse response = contactService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update contact", description = "Updates an existing contact by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contact updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Contact not found", content = @Content)
    })
    public ResponseEntity<ContactResponse> updateContact(
            @Parameter(description = "Contact ID", example = "1") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated contact information",
                    content = @Content(schema = @Schema(implementation = ContactUpdateRequest.class))
            )
            @Valid @RequestBody ContactUpdateRequest request) {
        log.info("Updating contact with ID: {}", id);
        ContactResponse response = contactService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete contact", description = "Deletes a contact by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Contact deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Contact not found", content = @Content)
    })
    public ResponseEntity<Void> deleteContact(
            @Parameter(description = "Contact ID", example = "1") @PathVariable Long id) {
        log.info("Deleting contact with ID: {}", id);
        contactService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get contact by ID", description = "Retrieves a specific contact by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contact found"),
            @ApiResponse(responseCode = "404", description = "Contact not found", content = @Content)
    })
    public ResponseEntity<ContactResponse> getContactById(
            @Parameter(description = "Contact ID", example = "1") @PathVariable Long id) {
        log.info("Getting contact by ID: {}", id);
        ContactResponse response = contactService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Get contacts by name", description = "Retrieves a list of contacts matching the name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contacts found (can be an empty list)")
    })
    public ResponseEntity<List<ContactResponse>> getContactsByName(
            @Parameter(description = "Contact name", example = "João da Silva") @PathVariable String name) {
        log.info("Getting contacts by name: {}", name);
        List<ContactResponse> response = contactService.findByName(name);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "List contacts", description = "Retrieves paginated list of contacts with optional search")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contacts retrieved successfully")
    })
    public ResponseEntity<PageResponse<ContactResponse>> getAllContacts(
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field", example = "name") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction", example = "asc") @RequestParam(defaultValue = "asc") String sortDir,
            @Parameter(description = "Search term", example = "John") @RequestParam(required = false) String search) {

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
    @Operation(summary = "Import contacts", description = "Imports contacts from CSV file")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contacts imported successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid file or processing error", content = @Content)
    })
    public ResponseEntity<List<ContactResponse>> importContacts(
            @Parameter(description = "CSV file containing contacts") @RequestParam("file") MultipartFile file) {
        log.info("Importing contacts from file: {}", file.getOriginalFilename());

        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        List<ContactResponse> importedContacts = contactService.importFromCsv(file);
        return ResponseEntity.ok(importedContacts);
    }

    @GetMapping("/export/excel")
    @Operation(summary = "Export to Excel", description = "Exports all contacts to Excel file")
    @ApiResponse(responseCode = "200", description = "Excel file generated successfully")
    public ResponseEntity<byte[]> exportToExcel() {
        log.info("Exporting contacts to Excel");
        byte[] excelData = contactService.exportToExcel();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "contacts.xlsx");

        return ResponseEntity.ok().headers(headers).body(excelData);
    }

    @GetMapping("/export/pdf")
    @Operation(summary = "Export to PDF", description = "Exports all contacts to PDF file")
    @ApiResponse(responseCode = "200", description = "PDF file generated successfully")
    public ResponseEntity<byte[]> exportToPdf() {
        log.info("Exporting contacts to PDF");
        byte[] pdfData = contactService.exportToPdf();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "contacts.pdf");

        return ResponseEntity.ok().headers(headers).body(pdfData);
    }
}