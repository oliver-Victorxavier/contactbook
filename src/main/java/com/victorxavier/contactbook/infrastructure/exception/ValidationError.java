package com.victorxavier.contactbook.infrastructure.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Validation error response with detailed field-level error information.
 * Extends StandardError to maintain consistency while adding validation specifics.
 *
 * @author Victor Xavier
 * @since 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidationError {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Instant timestamp;

    private Integer status;
    private String error;
    private String message;
    private String path;
    private String correlationId;
    private List<FieldMessage> errors;
    private Integer errorCount;

    public ValidationError() {
        this.timestamp = Instant.now();
        this.errors = new ArrayList<>();
        this.errorCount = 0;
    }

    public ValidationError(Integer status, String error, String message, String path) {
        this();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public ValidationError(Integer status, String error, String message, String path, String correlationId) {
        this();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.correlationId = correlationId;
    }

    /**
     * Adds a field validation error
     */
    public void addError(String fieldName, String message) {
        this.errors.add(new FieldMessage(fieldName, message));
        this.errorCount = this.errors.size();
    }

    /**
     * Adds multiple field validation errors
     */
    public void addErrors(List<FieldMessage> fieldMessages) {
        this.errors.addAll(fieldMessages);
        this.errorCount = this.errors.size();
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

    public List<FieldMessage> getErrors() {
        return errors;
    }

    public void setErrors(List<FieldMessage> errors) {
        this.errors = errors;
        this.errorCount = errors != null ? errors.size() : 0;
    }

    public Integer getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidationError that = (ValidationError) o;
        return Objects.equals(status, that.status) &&
                Objects.equals(error, that.error) &&
                Objects.equals(message, that.message) &&
                Objects.equals(path, that.path) &&
                Objects.equals(correlationId, that.correlationId) &&
                Objects.equals(errors, that.errors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, error, message, path, correlationId, errors);
    }

    @Override
    public String toString() {
        return "ValidationError{" +
                "timestamp=" + timestamp +
                ", status=" + status +
                ", error='" + error + '\'' +
                ", message='" + message + '\'' +
                ", path='" + path + '\'' +
                ", correlationId='" + correlationId + '\'' +
                ", errorCount=" + errorCount +
                ", errors=" + errors +
                '}';
    }
}