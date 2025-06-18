package com.victorxavier.contactbook.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name = "contact_book_tb")
@Schema(description = "Contact entity representing a person with address information")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Full name of the contact", example = "John Doe", required = true)
    private String name;

    @Schema(description = "Phone number", example = "(11) 99999-9999", required = true)
    private String phone;

    @Schema(description = "Brazilian postal code", example = "01001000", required = true)
    private String cep;

    @Schema(description = "Street address", example = "Praça da Sé", accessMode = Schema.AccessMode.READ_ONLY)
    private String logradouro;

    @Schema(description = "House/building number", example = "123", required = true)
    private Integer numero;

    @Schema(description = "Neighborhood", example = "Sé", accessMode = Schema.AccessMode.READ_ONLY)
    private String bairro;

   @Schema(description = "City", example = "São Paulo", accessMode = Schema.AccessMode.READ_ONLY)
    private String cidade;  

    @Schema(description = "State abbreviation", example = "SP", accessMode = Schema.AccessMode.READ_ONLY)
    private String estado;

    public Contact() {}
    public Contact(String name, String phone, String cep, Integer numero) {
        this.name = name;
        this.phone = phone;
        this.cep = cep;
        this.numero = numero;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setAddress(String logradouro, String bairro, String cidade, String estado) {
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
    }

    public void clearAddress() {
        this.logradouro = null;
        this.bairro = null;
        this.cidade = null;
        this.estado = null;

    }
}