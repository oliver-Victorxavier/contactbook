package com.victorxavier.contactbook.domain.repository;

import com.victorxavier.contactbook.domain.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ContactRepository {
    Contact save(Contact contact);
    List<Contact> saveAll(List<Contact> contacts);
    void deleteById(Long id);
    boolean existsById(Long id);
    Optional<Contact> findById(Long id);
    List<Contact> findAll();
    Page<Contact> findAll(Pageable pageable);
    List<Contact> findByName(String name);
    Page<Contact> findBySearchTerm(String search, Pageable pageable );
}