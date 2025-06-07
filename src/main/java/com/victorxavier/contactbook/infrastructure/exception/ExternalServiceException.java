package com.victorxavier.contactbook.infrastructure.exception;

/**
 * Exception thrown when external service integration fails.
 *
 * @author Victor Xavier
 * @since 1.0
 */
public class ExternalServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String serviceName;
    private final Integer httpStatus;

    public ExternalServiceException(String message) {
        super(message);
        this.serviceName = null;
        this.httpStatus = null;
    }

    public ExternalServiceException(String message, Throwable cause) {
        super(message, cause);
        this.serviceName = null;
        this.httpStatus = null;
    }

    /**
     * Enhanced constructor with service details
     */
    public ExternalServiceException(String message, String serviceName, Integer httpStatus) {
        super(message);
        this.serviceName = serviceName;
        this.httpStatus = httpStatus;
    }

    public ExternalServiceException(String message, String serviceName, Integer httpStatus, Throwable cause) {
        super(message, cause);
        this.serviceName = serviceName;
        this.httpStatus = httpStatus;
    }

    public String getServiceName() {
        return serviceName;
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }

    /**
     * Factory methods for common external service failures
     */
    public static ExternalServiceException viaCepUnavailable() {
        return new ExternalServiceException(
                "Serviço ViaCEP temporariamente indisponível. Tente novamente mais tarde.",
                "ViaCEP",
                500
        );
    }

    public static ExternalServiceException viaCepTimeout() {
        return new ExternalServiceException(
                "Timeout na consulta do CEP. Tente novamente.",
                "ViaCEP",
                408
        );
    }

    public static ExternalServiceException viaCepError(int status, Throwable cause) {
        return new ExternalServiceException(
                String.format("Erro no serviço ViaCEP (Status: %d)", status),
                "ViaCEP",
                status,
                cause
        );
    }
}