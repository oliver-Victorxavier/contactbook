package com.victorxavier.contactbook.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Base class for contact request DTOs with common fields and validations
 */
public abstract class BaseContactRequest extends BaseRequest implements IContactRequest {

    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    @Schema(description = "Full name of the contact.", example = "João da Silva")
    private String name;

    @Pattern(regexp = "^\\d{10,11}$", message = "Telefone deve conter 10 ou 11 dígitos")
    @Schema(description = "Contact's phone number (only digits).", example = "11999998888")
    private String phone;

    @Pattern(regexp = "^\\d{8}$", message = "CEP deve conter 8 dígitos")
    @Schema(description = "Brazilian postal code (CEP), 8 digits only.", example = "01001000")
    private String cep;

    @Min(value = 1, message = "Número deve ser maior que 0")
    @Schema(description = "Address number.", example = "123")
    private Integer numero;

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
    
    /**
     * Returns a clean CEP without any non-digit characters
     */
    public String getCleanCep() {
        return cep != null ? cep.replaceAll("[^0-9]", "") : null;
    }
}