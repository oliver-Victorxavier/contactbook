package com.victorxavier.contactbook.domain.entity;

/**
 * Base class for domain entities with address fields
 */
public abstract class BaseAddressEntity extends BaseEntity implements AddressHolder {

    private String logradouro;
    private String bairro;
    private String cidade;
    private String estado;

    protected BaseAddressEntity() {}
    
    protected BaseAddressEntity(Long id, String name, String phone, String cep, Integer numero) {
        super(id, name, phone, cep, numero);
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
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
    
    /**
     * Sets all address fields at once
     */
    public void setAddress(String logradouro, String bairro, String cidade, String estado) {
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
    }
    
    /**
     * Clears all address fields
     */
    public void clearAddress() {
        this.logradouro = null;
        this.bairro = null;
        this.cidade = null;
        this.estado = null;
    }
}