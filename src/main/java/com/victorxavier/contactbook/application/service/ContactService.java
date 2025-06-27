package com.victorxavier.contactbook.application.service;

import com.victorxavier.contactbook.application.dto.request.ContactRequest;
import com.victorxavier.contactbook.application.dto.request.ContactUpdateRequest;
import com.victorxavier.contactbook.application.dto.response.ContactResponse;
import com.victorxavier.contactbook.application.dto.response.ExportedFile;
import com.victorxavier.contactbook.application.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ContactService {

    ContactResponse save(ContactRequest request);

    ContactResponse update(Long id, ContactUpdateRequest request);

    void delete(Long id);

    ContactResponse findById(Long id);

    List<ContactResponse> findByName(String name);

    PageResponse<ContactResponse> findAll(Pageable pageable);

    PageResponse<ContactResponse> findBySearchTerm(String search, Pageable pageable);

    List<ContactResponse> importFromCsv(MultipartFile file);

    ExportedFile export(String format);
}