package com.victorxavier.contactbook.application.mapper;

import com.victorxavier.contactbook.application.dto.request.ContactRequest;
import com.victorxavier.contactbook.application.dto.request.ContactUpdateRequest;
import com.victorxavier.contactbook.application.dto.response.ContactResponse;
import com.victorxavier.contactbook.domain.entity.Contact;
import org.springframework.stereotype.Component;

@Component
public class ContactMapper {

    public Contact toEntity(ContactRequest request) {
        if (request == null) return null;

        return new Contact(request.getName(), request.getPhone(), request.getCleanCep(), request.getNumero());
    }

    public ContactResponse toResponse(Contact contact) {
        if (contact == null) return null;

        return new ContactResponse(
                contact.getId(),
                contact.getName(),
                contact.getPhone(),
                contact.getCep(),
                contact.getLogradouro(),
                contact.getNumero(),
                contact.getBairro(),
                contact.getCidade(),
                contact.getEstado()
        );
    }

    public void updateEntity(Contact contact, ContactUpdateRequest request) {
        if (request.getName() != null) {
            contact.setName(request.getName());
        }
        if (request.getPhone() != null) {
            contact.setPhone(request.getPhone());
        }
        if (request.getCep() != null) {
            contact.setCep(request.getCleanCep());
        }
        if (request.getNumero() != null) {
            contact.setNumero(request.getNumero());
        }
    }
}