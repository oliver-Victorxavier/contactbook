package com.victorxavier.contactbook.infrastructure.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

/**
 * Represents a field-level validation error message.
 * Used within ValidationError to provide detailed field validation feedback.
 *
 * @author Victor Xavier
 * @since 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FieldMessage {

	private String fieldName;
	private String message;

	public FieldMessage() {
	}

	public FieldMessage(String fieldName, String message) {
		this.fieldName = fieldName;
		this.message = message;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		FieldMessage that = (FieldMessage) o;
		return Objects.equals(fieldName, that.fieldName) &&
				Objects.equals(message, that.message);
	}

	@Override
	public int hashCode() {
		return Objects.hash(fieldName, message);
	}

	@Override
	public String toString() {
		return "FieldMessage{" +
				"fieldName='" + fieldName + '\'' +
				", message='" + message + '\'' +
				'}';
	}
}