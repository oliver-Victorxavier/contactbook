package com.victorxavier.contactbook.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.victorxavier.contactbook.application.mapper.ContactMapper;
import com.victorxavier.contactbook.domain.service.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.victorxavier.contactbook.application.dto.request.ContactRequest;
import com.victorxavier.contactbook.application.dto.response.ContactResponse;
import com.victorxavier.contactbook.domain.entity.Contact;
import com.victorxavier.contactbook.domain.repository.ContactRepository;
import com.victorxavier.contactbook.infrastructure.exception.AddressNotFoundException;
import com.victorxavier.contactbook.infrastructure.exception.ExternalServiceException;

@ExtendWith(MockitoExtension.class)
@DisplayName("ContactService Tests")
class ContactServiceTest {

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private AddressService addressService;

    @Mock
    private ContactMapper contactMapper;

    @InjectMocks
    private ContactServiceImpl contactService;

    private ContactRequest contactRequest;
    private Contact contact;
    private ContactResponse contactResponse;
    private AddressService.AddressInfo addressInfo;

    @BeforeEach
    void setUp() {
        contactRequest = new ContactRequest();
        contactRequest.setName("João da Silva");
        contactRequest.setPhone("11987654321");
        contactRequest.setCep("01001000");
        contactRequest.setNumero(123);

        addressInfo = new AddressService.AddressInfo(
                "Rua Teste", "Bairro Teste", "Cidade Teste", "ST"
        );

        contact = new Contact();
        contact.setId(1L);
        contact.setName("João da Silva");
        contact.setPhone("11987654321");
        contact.setCep("01001000");
        contact.setNumero(123);
        contact.setLogradouro("Rua Teste");
        contact.setBairro("Bairro Teste");
        contact.setCidade("Cidade Teste");
        contact.setEstado("ST");

        contactResponse = new ContactResponse();
        contactResponse.setId(1L);
        contactResponse.setName("João da Silva");
        contactResponse.setPhone("11987654321");
        contactResponse.setCep("01001000");
        contactResponse.setLogradouro("Rua Teste");
        contactResponse.setNumero(123);
        contactResponse.setBairro("Bairro Teste");
        contactResponse.setCidade("Cidade Teste");
        contactResponse.setEstado("ST");
    }

    @Test
    @DisplayName("Should save contact successfully")
    void shouldSaveContactSuccessfully() {

        when(contactMapper.toEntity(contactRequest)).thenReturn(contact);
        when(addressService.getAddressByCep("01001000")).thenReturn(addressInfo);
        when(contactRepository.save(any(Contact.class))).thenReturn(contact);
        when(contactMapper.toResponse(contact)).thenReturn(contactResponse);

        ContactResponse result = contactService.save(contactRequest);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("João da Silva");
        assertThat(result.getPhone()).isEqualTo("11987654321");

        verify(contactMapper).toEntity(contactRequest);
        verify(addressService).getAddressByCep("01001000");
        verify(contactRepository).save(any(Contact.class));
        verify(contactMapper).toResponse(contact);
    }

    @Test
    @DisplayName("Should throw AddressNotFoundException when CEP not found")
    void shouldThrowAddressNotFoundExceptionWhenCepNotFound() {

        when(contactMapper.toEntity(contactRequest)).thenReturn(contact);
        when(addressService.getAddressByCep("01001000"))
                .thenThrow(new AddressNotFoundException("CEP não encontrado: 01001000"));

        assertThrows(AddressNotFoundException.class, () -> {
            contactService.save(contactRequest);
        });

        verify(contactMapper).toEntity(contactRequest);
        verify(addressService).getAddressByCep("01001000");
        verify(contactRepository, never()).save(any());
        verify(contactMapper, never()).toResponse(any());
    }

    @Test
    @DisplayName("Should throw ExternalServiceException when service is unavailable")
    void shouldThrowExternalServiceExceptionWhenServiceUnavailable() {

        when(contactMapper.toEntity(contactRequest)).thenReturn(contact);
        when(addressService.getAddressByCep("01001000"))
                .thenThrow(new ExternalServiceException("Erro no serviço de consulta de CEP"));

        assertThrows(ExternalServiceException.class, () -> {
            contactService.save(contactRequest);
        });

        verify(contactMapper).toEntity(contactRequest);
        verify(addressService).getAddressByCep("01001000");
        verify(contactRepository, never()).save(any());
        verify(contactMapper, never()).toResponse(any());
    }
}