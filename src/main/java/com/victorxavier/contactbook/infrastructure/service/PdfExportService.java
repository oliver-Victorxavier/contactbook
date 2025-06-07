package com.victorxavier.contactbook.infrastructure.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.victorxavier.contactbook.domain.entity.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Service responsible for exporting contact data to PDF format.
 * Uses iText library for PDF generation with landscape orientation.
 *
 * @author Victor Xavier
 * @since 1.0
 */
@Service
public class PdfExportService {

    private static final Logger log = LoggerFactory.getLogger(PdfExportService.class);

    /**
     * Exports a list of contacts to PDF format as byte array.
     * PDF is generated in landscape orientation with formatted table.
     *
     * @param contacts List of contacts to export
     * @return byte[] PDF file content
     * @throws RuntimeException if export fails
     */
    public byte[] exportContacts(List<Contact> contacts) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, outputStream);

            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Lista de Contatos", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            PdfPTable table = new PdfPTable(8);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            float[] columnWidths = {1f, 2f, 1.5f, 1f, 2f, 0.8f, 1.5f, 1.5f};
            table.setWidths(columnWidths);

            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
            String[] headers = {"ID", "Nome", "Telefone", "CEP", "Logradouro", "Nº", "Cidade", "Estado"};

            for (String header : headers) {
                Phrase phrase = new Phrase(header, headerFont);
                table.addCell(phrase);
            }

            Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
            for (Contact contact : contacts) {
                table.addCell(new Phrase(String.valueOf(contact.getId()), dataFont));
                table.addCell(new Phrase(contact.getName(), dataFont));
                table.addCell(new Phrase(contact.getPhone(), dataFont));
                table.addCell(new Phrase(contact.getCep(), dataFont));
                table.addCell(new Phrase(contact.getLogradouro() != null ? contact.getLogradouro() : "", dataFont));
                table.addCell(new Phrase(contact.getNumero() != null ? contact.getNumero().toString() : "", dataFont));
                table.addCell(new Phrase(contact.getCidade() != null ? contact.getCidade() : "", dataFont));
                table.addCell(new Phrase(contact.getEstado() != null ? contact.getEstado() : "", dataFont));
            }

            document.add(table);
            document.close();

            return outputStream.toByteArray();

        } catch (Exception e) {
            log.error("Error exporting contacts to PDF", e);
            throw new RuntimeException("Erro ao exportar para PDF: " + e.getMessage());
        }
    }
}