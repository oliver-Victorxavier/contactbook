package com.victorxavier.contactbook.infrastructure.service;

import com.victorxavier.contactbook.domain.service.AddressService;
import com.victorxavier.contactbook.infrastructure.client.ViaCepClient;
import com.victorxavier.contactbook.infrastructure.client.response.AddressResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {

    private static final Logger log = LoggerFactory.getLogger(AddressServiceImpl.class);

    private final ViaCepClient viaCepClient;

    public AddressServiceImpl(ViaCepClient viaCepClient) {
        this.viaCepClient = viaCepClient;
    }

    @Override
    public AddressInfo getAddressByCep(String cep) {
        log.info("Fetching address for CEP: {}", cep);

        try {
            AddressResponse response = viaCepClient.getAddress(cep);

            if (response.getLogradouro() == null || response.getLogradouro().isEmpty()) {
                throw new IllegalArgumentException("CEP não encontrado");
            }

            return new AddressInfo(
                    response.getLogradouro(),
                    response.getBairro(),
                    response.getCidade(),
                    response.getEstado()
            );
        } catch (Exception ex) {
            log.error("Error fetching address for CEP: {}, error: {}", cep, ex.getMessage());
            throw new IllegalArgumentException("Erro ao buscar endereço para o CEP: " + cep);
        }
    }
}