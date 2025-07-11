package com.victorxavier.contactbook.application.dto.request;

import jakarta.validation.constraints.*;

public class ContactUpdateRequest {

    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String name;

    @Pattern(regexp = "^\\(?\\d{2}\\)?[\\s-]?\\d{4,5}-?\\d{4}$",
            message = "Formato de telefone inválido. Use: (11) 99999-9999 ou 11999999999")
    private String phone;

    @Pattern(regexp = "^\\d{5}-?\\d{3}$",
            message = "CEP deve estar no formato 00000-000 ou 00000000")
    private String cep;

    @Min(value = 1, message = "Número deve ser maior que 0")
    private Integer numero;

    public ContactUpdateRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public Integer getNumero() { return numero; }
    public void setNumero(Integer numero) { this.numero = numero; }

}