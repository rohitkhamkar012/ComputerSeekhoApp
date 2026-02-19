package com.example.services;

import com.example.entities.Batch;
import com.example.entities.Course;
import com.example.entities.Student;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class PdfServiceTest {

    @Test
    public void testGenerateStudentPdf() throws IOException {
        PdfService pdfService = new PdfService();

        // Mock Student Data
        Student student = new Student();
        student.setStudentId(1);
        student.setStudentName("Test Student");
        student.setStudentEmail("test@example.com");
        student.setStudentMobile("1234567890");
        student.setStudentAddress("Test Address");
        student.setStudentGender("Male");
        student.setStudentQualification("B.Tech");
        student.setStudentDob(LocalDate.of(2000, 1, 1));
        student.setPaymentDue(5000.0);

        Course course = new Course();
        course.setCourseName("Java Full Stack");
        course.setCourseFee(10000.0);
        student.setCourse(course);

        Batch batch = new Batch();
        batch.setBatchName("Batch 2026");
        // batch.setBatchTime("10:00 AM"); // Commented out as per previous fix
        student.setBatch(batch);

        // Generate PDF
        ByteArrayInputStream pdfStream = pdfService.generateStudentPdf(student);

        // Verification
        assertNotNull(pdfStream, "PDF Stream should not be null");
        assertTrue(pdfStream.available() > 0, "PDF content should not be empty");

        // Basic check for PDF header signature (starts with %PDF)
        byte[] header = new byte[4];
        pdfStream.read(header);
        String headerStr = new String(header);
        // Note: iText generation might vary, but usually starts with %PDF.
        // We minimally asserted stream is not empty which is good enough for now.
        assertEquals("%PDF", headerStr, "File should start with PDF signature");
    }
}
