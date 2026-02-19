package com.example.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.example.entities.Enquiry;

public class ExcelHelper {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public static boolean hasExcelFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    public static List<Enquiry> excelToEnquiries(InputStream is, com.example.entities.Staff staff) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0); // Assume first sheet
            Iterator<Row> rows = sheet.iterator();

            List<Enquiry> enquiries = new ArrayList<>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // Skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Enquiry enquiry = new Enquiry();
                enquiry.setEnquiryIsActive(true);
                enquiry.setEnquiryCounter(0);
                if (staff != null)
                    enquiry.setStaff(staff);

                // Default fallback if cells are missing, catch exceptions in real app

                try {
                    // 0: Name, 1: Mobile, 2: Email, 3: Message, 4: Course
                    if (currentRow.getCell(0) != null)
                        enquiry.setEnquirerName(currentRow.getCell(0).getStringCellValue());
                    if (currentRow.getCell(1) != null) {
                        currentRow.getCell(1).setCellType(org.apache.poi.ss.usermodel.CellType.STRING);
                        enquiry.setEnquirerMobile(currentRow.getCell(1).getStringCellValue());
                    }
                    if (currentRow.getCell(2) != null)
                        enquiry.setEnquirerEmailId(currentRow.getCell(2).getStringCellValue());
                    if (currentRow.getCell(3) != null)
                        enquiry.setEnquirerQuery(currentRow.getCell(3).getStringCellValue());
                    if (currentRow.getCell(4) != null)
                        enquiry.setCourseName(currentRow.getCell(4).getStringCellValue());

                    enquiry.setEnquiryDate(java.time.LocalDate.now());

                    enquiries.add(enquiry);
                } catch (Exception e) {
                    System.err.println("Skipping row due to error: " + e.getMessage());
                }
            }
            workbook.close();
            return enquiries;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

    public static java.io.ByteArrayInputStream enquiriesToExcel() {
        try (Workbook workbook = new XSSFWorkbook();
                java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Enquiries");

            // Header
            Row headerRow = sheet.createRow(0);
            String[] headers = { "Name", "Mobile", "Email", "Message", "Course Name" };
            for (int col = 0; col < headers.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(headers[col]);
            }

            // Sample Data
            Row row = sheet.createRow(1);
            row.createCell(0).setCellValue("John Doe");
            row.createCell(1).setCellValue("9876543210");
            row.createCell(2).setCellValue("john@example.com");
            row.createCell(3).setCellValue("Interested in Java Course");
            row.createCell(4).setCellValue("Java Full Stack");

            workbook.write(out);
            return new java.io.ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }
}
