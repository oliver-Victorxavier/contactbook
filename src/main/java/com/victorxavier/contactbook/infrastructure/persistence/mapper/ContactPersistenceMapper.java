package com.victorxavier.contactbook.infrastructure.persistence.mapper;

import com.victorxavier.contactbook.domain.entity.Contact;
import com.victorxavier.contactbook.infrastructure.persistence.entity.ContactJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class ContactPersistenceMapper {

    public ContactJpaEntity toJpaEntity(Contact contact) {
        if (contact == null) return null;
        ContactJpaEntity jpaEntity = new ContactJpaEntity();
        jpaEntity.setId(contact.getId());
        jpaEntity.setName(contact.getName());
        jpaEntity.setPhone(contact.getPhone());
        jpaEntity.setCep(contact.getCep());
        jpaEntity.setLogradouro(contact.getLogradouro());
        jpaEntity.setNumero(contact.getNumero());
        jpaEntity.setBairro(contact.getBairro());
        jpaEntity.setCidade(contact.getCidade());
        jpaEntity.setEstado(contact.getEstado());
        return jpaEntity;
    }

    public Contact toDomainEntity(ContactJpaEntity jpaEntity) {
        if (jpaEntity == null) return null;
        Contact contact = new Contact();
        contact.setId(jpaEntity.getId());
        contact.setName(jpaEntity.getName());
        contact.setPhone(jpaEntity.getPhone());
        contact.setCep(jpaEntity.getCep());
        contact.setLogradouro(jpaEntity.getLogradouro());
        contact.setNumero(jpaEntity.getNumero());
        contact.setBairro(jpaEntity.getBairro());
        contact.setCidade(jpaEntity.getCidade());
        contact.setEstado(jpaEntity.getEstado());
        return contact;
    }
}