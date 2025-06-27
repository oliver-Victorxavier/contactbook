package com.victorxavier.contactbook.infrastructure.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ValidationError> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpServletRequest request) {

        String correlationId = generateCorrelationId();
        log.warn("JSON parsing error [{}]: {}", correlationId, ex.getMessage());

        ValidationError validationError = new ValidationError(
                HttpStatus.BAD_REQUEST.value(),
                "JSON Format Error",
                "Erro de formato nos dados informados",
                request.getRequestURI(),
                correlationId
        );

        Throwable rootCause = getRootCause(ex);

        if (rootCause instanceof InvalidFormatException invalidFormatEx) {
            handleInvalidFormatException(invalidFormatEx, validationError);
        } else if (rootCause instanceof MismatchedInputException mismatchedEx) {
            handleMismatchedInputException(mismatchedEx, validationError);
        } else if (rootCause instanceof JsonParseException jsonParseEx) {
            validationError.addError("json", "JSON malformado: " + jsonParseEx.getOriginalMessage());
        } else if (rootCause instanceof JsonMappingException jsonMappingEx) {
            validationError.addError("json", "Erro de mapeamento JSON: " + jsonMappingEx.getOriginalMessage());
        } else {
            validationError.addError("request", "Dados da requisição inválidos. Verifique o formato JSON.");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationError);
    }

    private Throwable getRootCause(Throwable throwable) {
        Throwable cause = throwable;
        while (cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        return cause;
    }

    private void handleInvalidFormatException(InvalidFormatException ex, ValidationError validationError) {
        String fieldName = extractFieldName(ex);
        String targetType = ex.getTargetType().getSimpleName();
        String providedValue = String.valueOf(ex.getValue());

        String message = buildFieldErrorMessage(fieldName, targetType, providedValue);
        validationError.addError(fieldName, message);
    }

    private void handleMismatchedInputException(MismatchedInputException ex, ValidationError validationError) {
        String fieldName = extractFieldName(ex);
        validationError.addError(fieldName, "Tipo de dado incorreto para o campo: " + fieldName);
    }

    private String extractFieldName(JsonMappingException ex) {
        if (ex.getPath() != null && !ex.getPath().isEmpty()) {
            var reference = ex.getPath().get(ex.getPath().size() - 1);
            String fieldName = reference.getFieldName();
            return fieldName != null ? fieldName : "field_" + reference.getIndex();
        }
        return "unknown";
    }

    private String buildFieldErrorMessage(String fieldName, String targetType, String providedValue) {
        return switch (targetType.toLowerCase()) {
            case "integer", "int" ->
                    String.format("Campo '%s' deve ser um número inteiro. Valor informado: '%s'", fieldName, providedValue);
            case "long" ->
                    String.format("Campo '%s' deve ser um número inteiro longo. Valor informado: '%s'", fieldName, providedValue);
            case "double", "float" ->
                    String.format("Campo '%s' deve ser um número decimal. Valor informado: '%s'", fieldName, providedValue);
            case "boolean" ->
                    String.format("Campo '%s' deve ser verdadeiro ou falso. Valor informado: '%s'", fieldName, providedValue);
            default ->
                    String.format("Campo '%s' tem formato inválido. Valor informado: '%s'", fieldName, providedValue);
        };
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> handleValidationErrors(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

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

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String message = error.getDefaultMessage();
            validationError.addError(fieldName, message);
            log.debug("Validation error - Field: {}, Message: {}", fieldName, message);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationError);
    }

    @ExceptionHandler(ContactNotFoundException.class)
    public ResponseEntity<StandardError> handleContactNotFound(
            ContactNotFoundException ex, HttpServletRequest request) {

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
    public ResponseEntity<StandardError> handleAddressNotFound(
            AddressNotFoundException ex, HttpServletRequest request) {

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
    public ResponseEntity<StandardError> handleExternalServiceError(
            ExternalServiceException ex, HttpServletRequest request) {

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
    public ResponseEntity<StandardError> handleCsvProcessingError(
            CsvProcessingException ex, HttpServletRequest request) {

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

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandardError> handleIllegalArgument(
            IllegalArgumentException ex, HttpServletRequest request) {

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
    public ResponseEntity<StandardError> handleRuntimeException(
            RuntimeException ex, HttpServletRequest request) {

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
    public ResponseEntity<StandardError> handleGenericException(
            Exception ex, HttpServletRequest request) {

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
}