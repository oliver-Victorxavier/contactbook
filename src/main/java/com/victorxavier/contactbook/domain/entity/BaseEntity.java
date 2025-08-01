package com.victorxavier.contactbook.domain.entity;

/**
 * Base class for domain entities
 */
public abstract class BaseEntity implements IBaseEntity {

    private Long id;
    private String name;
    private String phone;
    private String cep;
    private Integer numero;

    protected BaseEntity() {}
    
    protected BaseEntity(Long id, String name, String phone, String cep, Integer numero) {
        this.id = id;
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

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }
}