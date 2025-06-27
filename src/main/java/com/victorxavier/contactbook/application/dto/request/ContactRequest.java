package com.victorxavier.contactbook.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Request payload for creating a new contact")
public class ContactRequest {

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Schema(description = "Full name of the contact.", example = "João da Silva", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank(message = "Phone cannot be blank")
    @Pattern(regexp = "\\d{10,11}", message = "Phone must contain 10 or 11 digits")
    @Schema(description = "Contact's phone number (only digits).", example = "11999998888", requiredMode = Schema.RequiredMode.REQUIRED)
    private String phone;

    @NotBlank(message = "CEP cannot be blank")
    @Pattern(regexp = "\\d{8}", message = "CEP must contain 8 digits")
    @Schema(description = "Brazilian postal code (CEP), 8 digits only.", example = "01001000", requiredMode = Schema.RequiredMode.REQUIRED)
    private String cep;

    @NotNull(message = "Address number cannot be null")
    @Schema(description = "Address number.", example = "123", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer numero;

    public ContactRequest() {}
    public ContactRequest(String name, String phone, String cep, Integer numero) {
        this.name = name;
        this.phone = phone;
        this.cep = cep;
        this.numero = numero;
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
