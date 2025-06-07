package com.victorxavier.contactbook.infrastructure.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.Objects;

/**
 * Standard error response structure for the Contact Book API.
 * Provides consistent error formatting across all endpoints.
 *
 * @author Victor Xavier
 * @since 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StandardError {

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
	private Instant timestamp;

	private Integer status;
	private String error;
	private String message;
	private String path;
	private String correlationId;

	public StandardError() {
		this.timestamp = Instant.now();
	}

	public StandardError(Integer status, String error, String message, String path) {
		this();
		this.status = status;
		this.error = error;
		this.message = message;
		this.path = path;
	}

	/**
	 * Enhanced constructor with correlation ID for request tracking
	 */
	public StandardError(Integer status, String error, String message, String path, String correlationId) {
		this();
		this.status = status;
		this.error = error;
		this.message = message;
		this.path = path;
		this.correlationId = correlationId;
	}

	// Getters and Setters
	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		StandardError that = (StandardError) o;
		return Objects.equals(status, that.status) &&
				Objects.equals(error, that.error) &&
				Objects.equals(message, that.message) &&
				Objects.equals(path, that.path) &&
				Objects.equals(correlationId, that.correlationId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(status, error, message, path, correlationId);
	}

	@Override
	public String toString() {
		return "StandardError{" +
				"timestamp=" + timestamp +
				", status=" + status +
				", error='" + error + '\'' +
				", message='" + message + '\'' +
				", path='" + path + '\'' +
				", correlationId='" + correlationId + '\'' +
				'}';
	}
}