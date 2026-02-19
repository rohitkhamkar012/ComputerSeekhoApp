package com.example.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.DTO.ApiResponse;
import com.example.DTO.StudentDto;
import com.example.entities.Student;
import com.example.services.StudentService;

@RestController
@RequestMapping("/student")
@CrossOrigin("*")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping("/get/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable int id) {
        Optional<Student> student = studentService.getStudentById(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(student.get());
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudentsEntity();
        if (students == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(students);
    }

    @Autowired
    private com.example.services.EmailService emailService;

    @PostMapping("/add/{enquiryId}")
    public ResponseEntity<Student> addStudent(@RequestBody Student student, @PathVariable int enquiryId) {
        Student student2 = studentService.addStudent(student, enquiryId);

        // Reload student to ensure Course and Batch details are fully populated
        Optional<Student> fullStudent = studentService.getStudentById(student2.getStudentId());
        if (fullStudent.isPresent()) {
            student2 = fullStudent.get();
        }

        // Send Email (Direct Service)
        String subject = "Welcome to Computer Seekho - " + student2.getStudentName();
        String body = "Dear " + student2.getStudentName() + ",\n\n" +
                "Congratulations! You have been successfully registered.\n" +
                "Course: " + (student2.getCourse() != null ? student2.getCourse().getCourseName() : "N/A") + "\n" +
                "\nBest Regards,\nComputer Seekho Team";

        emailService.sendSimpleEmail(student2.getStudentEmail(), subject, body);

        return ResponseEntity.status(HttpStatus.CREATED).body(student2);
    }

    @PutMapping("/update")
    public ResponseEntity<Student> updateStudent(@RequestBody Student student) {
        Student student2 = studentService.updateStudent(student, student.getStudentId());
        return ResponseEntity.status(HttpStatus.CREATED).body(student2);
    }

    @DeleteMapping("/delete/{studentId}")
    public ResponseEntity<ApiResponse> deleteStudent(@PathVariable int studentId) {
        studentService.deleteStudent(studentId);
        return new ResponseEntity<>(new ApiResponse("Student deleted successfully", LocalDateTime.now()),
                HttpStatus.OK);
    }

    @GetMapping("/getbybatch/{batchid}")
    public ResponseEntity<List<Student>> getbybatch(@PathVariable int batchid) {
        List<Student> students = studentService.getbybatch(batchid);
        if (students == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(students);
    }

    @GetMapping("/getbycourse/{courseid}")
    public ResponseEntity<List<Student>> getbycourse(@PathVariable int courseid) {
        List<Student> students = studentService.getbycourse(courseid);
        if (students == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(students);
    }

    @Autowired
    private com.example.services.PdfService pdfService;

    @GetMapping(value = "/pdf/{id}", produces = org.springframework.http.MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<org.springframework.core.io.InputStreamResource> studentPdf(@PathVariable int id) {
        Optional<Student> student = studentService.getStudentById(id);

        if (student.isPresent()) {
            java.io.ByteArrayInputStream bis = pdfService.generateStudentPdf(student.get());

            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=student_" + id + ".pdf");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                    .body(new org.springframework.core.io.InputStreamResource(bis));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}