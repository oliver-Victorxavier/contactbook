package com.victorxavier.contactbook.infrastructure.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.victorxavier.contactbook.application.port.out.ContactExportPort;
import com.victorxavier.contactbook.domain.entity.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

@Service
public class PdfExportService implements ContactExportPort {

    private static final Logger log = LoggerFactory.getLogger(PdfExportService.class);

    @Override
    public byte[] export(List<Contact> contacts) {
        log.info("Generating PDF export for {} contacts", contacts.size());
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, outputStream);

            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Paragraph title = new Paragraph("Lista de Contatos", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            PdfPTable table = new PdfPTable(8);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{0.5f, 2f, 1.5f, 1f, 2f, 0.5f, 1.5f, 1.5f});

            addTableHeader(table);
            addTableRows(table, contacts);

            document.add(table);
            document.close();

            log.info("PDF export generated successfully.");
            return outputStream.toByteArray();

        } catch (Exception e) {
            log.error("Error exporting contacts to PDF", e);
            throw new RuntimeException("Erro ao exportar para PDF: " + e.getMessage(), e);
        }
    }

    private void addTableHeader(PdfPTable table) {
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
        Stream.of("ID", "Nome", "Telefone", "CEP", "Logradouro", "Nº", "Cidade", "Estado")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(new BaseColor(52, 152, 219)); // Cor azul
                    header.setBorderWidth(1);
                    header.setPhrase(new Phrase(columnTitle, headerFont));
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    header.setPadding(5);
                    table.addCell(header);
                });
    }

    private void addTableRows(PdfPTable table, List<Contact> contacts) {
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
    }

    @Override
    public String getMimeType() {
        return "application/pdf";
    }

    @Override
    public String getFilename() {
        return "contacts.pdf";
    }

    @Override
    public boolean canHandle(String format) {
        return "pdf".equalsIgnoreCase(format);
    }
}