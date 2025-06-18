package com.victorxavier.contactbook.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Request payload for creating a new contact")
public class ContactRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Schema(description = "Full name of the contact", example = "John Doe", required = true)
    private String name;

    @NotBlank(message = "Phone is required")
    @Pattern(
            regexp = "^\\(?\\d{2}\\)?[\\s-]?\\d{4,5}-?\\d{4}$",
            message = "Invalid phone format. Use: (11) 99999-9999 or 11999999999"
    )
    @Schema(description = "Phone number in Brazilian format", example = "(11) 99999-9999", required = true)
    private String phone;
    @NotBlank(message = "CEP is required")
    @Pattern(
            regexp = "^\\d{5}-?\\d{3}$",
            message = "CEP must be in format 00000-000 or 00000000"
    )
    @Schema(description = "Brazilian postal code", example = "01001-000", required = true)
    private String cep;

    @NotNull(message = "Number is required")
    @Min(value = 1, message = "Number must be greater than 0")
    @Schema(description = "House or building number", example = "123", required = true)
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
