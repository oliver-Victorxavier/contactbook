package com.victorxavier.contactbook.infrastructure.exception;

/**
 * Exception thrown when CSV file processing fails.
 *
 * @author Victor Xavier
 * @since 1.0
 */
public class CsvProcessingException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String fileName;
    private final Integer lineNumber;

    public CsvProcessingException(String message) {
        super(message);
        this.fileName = null;
        this.lineNumber = null;
    }

    public CsvProcessingException(String message, Throwable cause) {
        super(message, cause);
        this.fileName = null;
        this.lineNumber = null;
    }

    public CsvProcessingException(String message, String fileName, Integer lineNumber) {
        super(message);
        this.fileName = fileName;
        this.lineNumber = lineNumber;
    }

    public String getFileName() {
        return fileName;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public static CsvProcessingException invalidFormat(String fileName) {
        return new CsvProcessingException(
                "Formato de arquivo CSV inválido",
                fileName,
                null
        );
    }

    public static CsvProcessingException invalidLine(String fileName, int lineNumber, String reason) {
        return new CsvProcessingException(
                String.format("Erro na linha %d: %s", lineNumber, reason),
                fileName,
                lineNumber
        );
    }
}