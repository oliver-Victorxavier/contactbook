package com.victorxavier.contactbook.infrastructure.persistence.adapter;

import com.victorxavier.contactbook.domain.entity.Contact;
import com.victorxavier.contactbook.domain.repository.ContactRepository;
import com.victorxavier.contactbook.infrastructure.persistence.entity.ContactJpaEntity;
import com.victorxavier.contactbook.infrastructure.persistence.mapper.ContactPersistenceMapper;
import com.victorxavier.contactbook.infrastructure.persistence.repository.ContactJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ContactRepositoryAdapter implements ContactRepository {

    private final ContactJpaRepository jpaRepository;
    private final ContactPersistenceMapper mapper;

    public ContactRepositoryAdapter(ContactJpaRepository jpaRepository, ContactPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Contact save(Contact contact) {
        ContactJpaEntity jpaEntity = mapper.toJpaEntity(contact);
        ContactJpaEntity savedEntity = jpaRepository.save(jpaEntity);
        return mapper.toDomainEntity(savedEntity);
    }

    @Override
    public List<Contact> saveAll(List<Contact> contacts) {
        List<ContactJpaEntity> jpaEntities = contacts.stream().map(mapper::toJpaEntity).collect(Collectors.toList());
        List<ContactJpaEntity> savedEntities = jpaRepository.saveAll(jpaEntities);
        return savedEntities.stream().map(mapper::toDomainEntity).collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public Optional<Contact> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomainEntity);
    }

    @Override
    public List<Contact> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomainEntity).collect(Collectors.toList());
    }

    @Override
    public Page<Contact> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(mapper::toDomainEntity);
    }

    @Override
    public List<Contact> findByName(String name) {
        return jpaRepository.findByNameContainingIgnoreCase(name).stream()
                .map(mapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Contact> findBySearchTerm(String search, Pageable pageable) {
        return jpaRepository.findBySearchTerm(search, pageable).map(mapper::toDomainEntity);
    }
}