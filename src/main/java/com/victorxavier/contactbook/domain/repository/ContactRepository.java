package com.victorxavier.contactbook.domain.repository;

import com.victorxavier.contactbook.domain.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByName(String name);

    Page<Contact> findAll(Pageable pageable);

    @Query("SELECT c FROM Contact c WHERE " +
            "LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.cidade) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.bairro) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Contact> findBySearchTerm(@Param("search") String search, Pageable pageable);
}