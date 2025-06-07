package com.victorxavier.contactbook.application.service;

import com.victorxavier.contactbook.application.dto.request.ContactRequest;
import com.victorxavier.contactbook.application.dto.request.ContactUpdateRequest;
import com.victorxavier.contactbook.application.dto.response.ContactResponse;
import com.victorxavier.contactbook.application.dto.response.PageResponse;
import com.victorxavier.contactbook.infrastructure.exception.ContactNotFoundException;
import com.victorxavier.contactbook.infrastructure.exception.AddressNotFoundException;
import com.victorxavier.contactbook.infrastructure.exception.ExternalServiceException;
import com.victorxavier.contactbook.infrastructure.exception.CsvProcessingException;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Service interface for Contact management operations.
 * Provides CRUD operations, search functionality, and import/export features.
 *
 * @author Victor Xavier
 * @since 1.0
 */
public interface ContactService {

    /**
     * Creates a new contact with automatic address resolution via CEP.
     *
     * @param request Contact data for creation
     * @return Created contact response
     * @throws IllegalArgumentException if CEP is null, empty, or has invalid format
     * @throws AddressNotFoundException if CEP is not found in external service
     * @throws ExternalServiceException if address service is unavailable
     */
    ContactResponse save(ContactRequest request);

    /**
     * Updates an existing contact. Re-fetches address if CEP is modified.
     *
     * @param id Contact ID to update
     * @param request Updated contact data
     * @return Updated contact response
     * @throws ContactNotFoundException if contact with given ID is not found
     * @throws IllegalArgumentException if new CEP has invalid format
     * @throws AddressNotFoundException if new CEP is not found in external service
     * @throws ExternalServiceException if address service is unavailable
     */
    ContactResponse update(Long id, ContactUpdateRequest request);

    /**
     * Deletes a contact by ID.
     *
     * @param id Contact ID to delete
     * @throws ContactNotFoundException if contact with given ID is not found
     */
    void delete(Long id);

    /**
     * Retrieves a contact by ID.
     *
     * @param id Contact ID to search
     * @return Contact response
     * @throws ContactNotFoundException if contact with given ID is not found
     */
    ContactResponse findById(Long id);

    /**
     * Retrieves a contact by name.
     *
     * @param name Contact name to search
     * @return Contact response
     * @throws ContactNotFoundException if contact with given name is not found
     */
    ContactResponse findByName(String name);

    /**
     * Retrieves all contacts with pagination support.
     *
     * @param pageable Pagination parameters
     * @return Paginated contact response
     */
    PageResponse<ContactResponse> findAll(Pageable pageable);

    /**
     * Searches contacts by term (name, city, or neighborhood) with pagination.
     *
     * @param search Search term to filter contacts
     * @param pageable Pagination parameters
     * @return Paginated contact response matching the search term
     */
    PageResponse<ContactResponse> findBySearchTerm(String search, Pageable pageable);

    /**
     * Imports contacts from a CSV file.
     * Expected CSV format: Name,Phone,CEP,Number (with header row).
     *
     * @param file CSV file containing contact data
     * @return List of imported contacts
     * @throws CsvProcessingException if CSV file processing fails
     * @throws IllegalArgumentException if file is empty or has invalid format
     */
    List<ContactResponse> importFromCsv(MultipartFile file);

    /**
     * Exports all contacts to Excel format (.xlsx).
     *
     * @return Excel file content as byte array
     * @throws RuntimeException if Excel generation fails
     */
    byte[] exportToExcel();

    /**
     * Exports all contacts to PDF format.
     * Generated in landscape orientation with formatted table.
     *
     * @return PDF file content as byte array
     * @throws RuntimeException if PDF generation fails
     */
    byte[] exportToPdf();
}