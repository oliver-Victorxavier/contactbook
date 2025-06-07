package com.victorxavier.contactbook.domain.service;

import com.victorxavier.contactbook.infrastructure.exception.AddressNotFoundException;
import com.victorxavier.contactbook.infrastructure.exception.ExternalServiceException;

/**
 * Domain service interface for address operations.
 * Provides address information lookup by Brazilian postal code (CEP).
 *
 * @author Victor Xavier
 * @since 1.0
 */
public interface AddressService {

    /**
     * Retrieves address information by CEP.
     *
     * @param cep Brazilian postal code (8 digits)
     * @return AddressInfo containing address details
     * @throws AddressNotFoundException if CEP is not found
     * @throws ExternalServiceException if external service fails
     * @throws IllegalArgumentException if CEP format is invalid
     */
    AddressInfo getAddressByCep(String cep);

    /**
     * Immutable data class containing address information.
     */
    class AddressInfo {
        private final String logradouro;
        private final String bairro;
        private final String cidade;
        private final String estado;

        public AddressInfo(String logradouro, String bairro, String cidade, String estado) {
            this.logradouro = logradouro;
            this.bairro = bairro;
            this.cidade = cidade;
            this.estado = estado;
        }

        public String getLogradouro() {
            return logradouro;
        }

        public String getBairro() {
            return bairro;
        }

        public String getCidade() {
            return cidade;
        }

        public String getEstado() {
            return estado;
        }
    }
}