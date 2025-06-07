package com.victorxavier.contactbook.infrastructure.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ContactNotFoundException.class)
    public ResponseEntity<StandardError> handleContactNotFound(ContactNotFoundException ex, HttpServletRequest request) {
        String correlationId = generateCorrelationId();
        log.warn("Contact not found [{}]: {} (Identifier: {})",
                correlationId, ex.getMessage(), ex.getContactIdentifier());

        StandardError error = new StandardError(
                HttpStatus.NOT_FOUND.value(),
                "Resource Not Found",
                ex.getMessage(),
                request.getRequestURI(),
                correlationId
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<StandardError> handleAddressNotFound(AddressNotFoundException ex, HttpServletRequest request) {
        String correlationId = generateCorrelationId();
        log.warn("Address not found [{}]: {} (CEP: {})",
                correlationId, ex.getMessage(), ex.getCep());

        StandardError error = new StandardError(
                HttpStatus.NOT_FOUND.value(),
                "Address Not Found",
                ex.getMessage(),
                request.getRequestURI(),
                correlationId
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<StandardError> handleExternalServiceError(ExternalServiceException ex, HttpServletRequest request) {
        String correlationId = generateCorrelationId();
        log.error("External service error [{}]: {} (Service: {}, Status: {})",
                correlationId, ex.getMessage(), ex.getServiceName(), ex.getHttpStatus());

        StandardError error = new StandardError(
                HttpStatus.BAD_GATEWAY.value(),
                "External Service Error",
                ex.getMessage(),
                request.getRequestURI(),
                correlationId
        );

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(error);
    }

    @ExceptionHandler(CsvProcessingException.class)
    public ResponseEntity<StandardError> handleCsvProcessingError(CsvProcessingException ex, HttpServletRequest request) {
        String correlationId = generateCorrelationId();
        log.warn("CSV processing error [{}]: {} (File: {}, Line: {})",
                correlationId, ex.getMessage(), ex.getFileName(), ex.getLineNumber());

        StandardError error = new StandardError(
                HttpStatus.BAD_REQUEST.value(),
                "CSV Processing Error",
                ex.getMessage(),
                request.getRequestURI(),
                correlationId
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String correlationId = generateCorrelationId();
        log.warn("Validation errors [{}]: {} field(s) invalid",
                correlationId, ex.getBindingResult().getFieldErrorCount());

        ValidationError validationError = new ValidationError(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error",
                "Erro de validação nos campos informados",
                request.getRequestURI(),
                correlationId
        );

        ex.getBindingResult().getFieldErrors().forEach(error ->
                validationError.addError(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationError);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandardError> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        String correlationId = generateCorrelationId();
        log.warn("Illegal argument [{}]: {}", correlationId, ex.getMessage());

        StandardError error = new StandardError(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI(),
                correlationId
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<StandardError> handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
        String correlationId = generateCorrelationId();
        log.error("Runtime exception [{}]: {}", correlationId, ex.getMessage(), ex);

        StandardError error = new StandardError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Erro interno da aplicação",
                request.getRequestURI(),
                correlationId
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> handleGenericException(Exception ex, HttpServletRequest request) {
        String correlationId = generateCorrelationId();
        log.error("Unexpected error [{}]: {}", correlationId, ex.getMessage(), ex);

        StandardError error = new StandardError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Erro interno do servidor",
                request.getRequestURI(),
                correlationId
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    private String generateCorrelationId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    @ExceptionHandler(ContactValidationException.class)
    public ResponseEntity<ValidationError> handleContactValidation(ContactValidationException ex, HttpServletRequest request) {
        String correlationId = generateCorrelationId();
        log.warn("Contact validation errors [{}]: {} field(s) invalid",
                correlationId, ex.getFieldErrors().size());

        ValidationError validationError = new ValidationError(
                HttpStatus.BAD_REQUEST.value(),
                "Contact Validation Error",
                "Dados do contato são inválidos",
                request.getRequestURI(),
                correlationId
        );

        // Adiciona os erros de campo
        validationError.addErrors(ex.getFieldErrors());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationError);
    }
}