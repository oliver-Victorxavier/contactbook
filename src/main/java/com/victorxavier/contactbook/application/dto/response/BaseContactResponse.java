package com.victorxavier.contactbook.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Base class for contact response DTOs with common fields
 */
public abstract class BaseContactResponse extends BaseResponse {

    @Schema(description = "Unique identifier of the contact.", example = "1")
    private Long id;

    @Schema(description = "Full name of the contact.", example = "João da Silva")
    private String name;

    @Schema(description = "Contact's phone number.", example = "11999998888")
    private String phone;

    @Schema(description = "Brazilian postal code (CEP).", example = "01001000")
    private String cep;

    @Schema(description = "Street name, automatically fetched from CEP.", example = "Praça da Sé")
    private String logradouro;

    @Schema(description = "Address number.", example = "123")
    private Integer numero;

    @Schema(description = "Neighborhood, automatically fetched from CEP.", example = "Sé")
    private String bairro;

    @Schema(description = "City, automatically fetched from CEP.", example = "São Paulo")
    private String cidade;

    @Schema(description = "State, automatically fetched from CEP.", example = "SP")
    private String estado;
    
    @Schema(description = "Complete formatted address.", example = "Praça da Sé, 123 - Sé, São Paulo - SP")
    private String enderecoCompleto;

    public BaseContactResponse() {}
    
    public BaseContactResponse(Long id, String name, String phone, String cep,
                           String logradouro, Integer numero, String bairro,
                           String cidade, String estado) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.cep = cep;
        this.logradouro = logradouro;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.enderecoCompleto = buildEnderecoCompleto();
    }

    /**
     * Builds the complete address string
     */
    protected String buildEnderecoCompleto() {
        return null; // Default implementation returns null, override in subclasses
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public String getLogradouro() { return logradouro; }
    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
        this.enderecoCompleto = buildEnderecoCompleto();
    }

    public Integer getNumero() { return numero; }
    public void setNumero(Integer numero) {
        this.numero = numero;
        this.enderecoCompleto = buildEnderecoCompleto();
    }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) {
        this.bairro = bairro;
        this.enderecoCompleto = buildEnderecoCompleto();
    }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) {
        this.cidade = cidade;
        this.enderecoCompleto = buildEnderecoCompleto();
    }

    public String getEstado() { return estado; }
    public void setEstado(String estado) {
        this.estado = estado;
        this.enderecoCompleto = buildEnderecoCompleto();
    }

    public String getEnderecoCompleto() { return enderecoCompleto; }
    public void setEnderecoCompleto(String enderecoCompleto) {
        this.enderecoCompleto = enderecoCompleto;
    }
}