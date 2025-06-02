package com.victorxavier.contactbook.domain.service;

public interface AddressService {

    AddressInfo getAddressByCep(String cep);

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