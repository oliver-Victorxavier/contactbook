package com.victorxavier.contactbook.infrastructure.service;

import com.victorxavier.contactbook.domain.service.AddressService;
import com.victorxavier.contactbook.infrastructure.client.ViaCepClient;
import com.victorxavier.contactbook.infrastructure.client.response.AddressResponse;
import com.victorxavier.contactbook.infrastructure.exception.AddressNotFoundException;
import com.victorxavier.contactbook.infrastructure.exception.ExternalServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import feign.FeignException;

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

        validateCep(cep);
        String cleanCep = cleanCep(cep);

        try {
            AddressResponse response = viaCepClient.getAddress(cleanCep);

            if (response == null || isInvalidResponse(response)) {
                log.warn("CEP not found or invalid response for CEP: {}", cleanCep);
                throw new AddressNotFoundException("CEP não encontrado: " + cep);
            }

            log.info("Address found successfully for CEP: {}", cleanCep);

            return new AddressInfo(
                    response.getLogradouro(),
                    response.getBairro(),
                    response.getCidade(),
                    response.getEstado()
            );

        } catch (FeignException.NotFound ex) {
            log.warn("CEP not found (Feign): {}", cleanCep);
            throw new AddressNotFoundException("CEP não encontrado: " + cep);

        } catch (FeignException ex) {
            log.error("External service error for CEP: {}, status: {}", cleanCep, ex.status());
            throw new ExternalServiceException("Erro no serviço de consulta de CEP. Tente novamente mais tarde.");

        } catch (AddressNotFoundException | IllegalArgumentException ex) {
            // Re-throw domain exceptions
            throw ex;
            
        } catch (Exception ex) {
            log.error("Unexpected error fetching address for CEP: {}", cleanCep, ex);
            throw new ExternalServiceException("Erro inesperado ao buscar endereço para o CEP: " + cep);
        }
    }

    private void validateCep(String cep) {
        if (cep == null || cep.trim().isEmpty()) {
            throw new IllegalArgumentException("CEP não pode ser nulo ou vazio.");
        }

        String cleanCep = cep.replaceAll("[^0-9]", "");
        if (cleanCep.length() != 8) {
            throw new IllegalArgumentException("CEP deve conter exatamente 8 dígitos.");
        }
    }

    private String cleanCep(String cep) {
        return cep.replaceAll("[^0-9]", "");
    }

    private boolean isInvalidResponse(AddressResponse response) {
        if (Boolean.TRUE.equals(response.getErro())) {
            return true;
        }
        return response.getLogradouro() == null ||
                response.getLogradouro().trim().isEmpty();
    }
}