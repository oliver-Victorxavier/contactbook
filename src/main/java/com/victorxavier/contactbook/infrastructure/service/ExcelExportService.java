package com.victorxavier.contactbook.infrastructure.service;

import com.victorxavier.contactbook.domain.entity.Contact;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Service responsible for exporting contact data to Excel format (.xlsx).
 * Uses Apache POI library for Excel file generation.
 *
 * @author Victor Xavier
 * @since 1.0
 */
@Service
public class ExcelExportService {

    private static final Logger log = LoggerFactory.getLogger(ExcelExportService.class);

    /**
     * Exports a list of contacts to Excel format as byte array.
     *
     * @param contacts List of contacts to export
     * @return byte[] Excel file content
     * @throws RuntimeException if export fails
     */
    public byte[] exportContacts(List<Contact> contacts) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Contatos");

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Nome", "Telefone", "CEP", "Logradouro", "Número", "Bairro", "Cidade", "Estado"};

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            for (int i = 0; i < contacts.size(); i++) {
                Contact contact = contacts.get(i);
                Row row = sheet.createRow(i + 1);

                row.createCell(0).setCellValue(contact.getId());
                row.createCell(1).setCellValue(contact.getName());
                row.createCell(2).setCellValue(contact.getPhone());
                row.createCell(3).setCellValue(contact.getCep());
                row.createCell(4).setCellValue(contact.getLogradouro());
                row.createCell(5).setCellValue(contact.getNumero());
                row.createCell(6).setCellValue(contact.getBairro());
                row.createCell(7).setCellValue(contact.getCidade());
                row.createCell(8).setCellValue(contact.getEstado());
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();

        } catch (Exception e) {
            log.error("Error exporting contacts to Excel", e);
            throw new RuntimeException("Erro ao exportar para Excel: " + e.getMessage());
        }
    }
}