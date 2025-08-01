package com.victorxavier.contactbook.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Request payload for creating a new contact")
public class ContactRequest extends BaseContactRequest {

    @NotBlank(message = "Name cannot be blank")
    @Override
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    public String getName() {
        return super.getName();
    }

    @NotBlank(message = "Phone cannot be blank")
    @Override
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    public String getPhone() {
        return super.getPhone();
    }

    @NotBlank(message = "CEP cannot be blank")
    @Override
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    public String getCep() {
        return super.getCep();
    }

    @NotNull(message = "Address number cannot be null")
    @Override
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    public Integer getNumero() {
        return super.getNumero();
    }

    public ContactRequest() {}
    
    public ContactRequest(String name, String phone, String cep, Integer numero) {
        setName(name);
        setPhone(phone);
        setCep(cep);
        setNumero(numero);
    }
}
