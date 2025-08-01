package com.victorxavier.contactbook.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request payload for updating an existing contact")
public class ContactUpdateRequest extends BaseContactRequest {
    // All fields and validations are inherited from BaseContactRequest
    // No additional validations needed for update request
}