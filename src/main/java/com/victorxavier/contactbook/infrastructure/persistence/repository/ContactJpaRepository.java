package com.victorxavier.contactbook.infrastructure.persistence.repository;

import com.victorxavier.contactbook.infrastructure.persistence.entity.ContactJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactJpaRepository extends JpaRepository<ContactJpaEntity, Long> {

    List<ContactJpaEntity> findByNameContainingIgnoreCase(String name);

    @Query("SELECT c FROM ContactJpaEntity c WHERE " +
            "LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.cidade) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.bairro) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<ContactJpaEntity> findBySearchTerm(@Param("search") String search, Pageable pageable);
}