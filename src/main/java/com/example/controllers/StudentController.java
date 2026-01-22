package com.example.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.services.StudentService;
import com.example.DTO.ApiResponse;
import com.example.DTO.StudentDto;
import com.example.entities.Student;

@RestController
@RequestMapping("/student")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentService studentService;

    // 1. Get Student by ID
    @GetMapping("/get/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable int id) {
        Optional<Student> student = studentService.getStudentById(id);
        
        return student.map(value -> ResponseEntity.ok().body(value))
                      .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // 2. Get All Students
    @GetMapping("/getAll")
    public ResponseEntity<List<StudentDto>> getAllStudents() {
        List<StudentDto> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    // 3. Add Student
    @PostMapping("/add/{enquiryId}")
    public ResponseEntity<ApiResponse> addStudent(@RequestBody Student student, @PathVariable int enquiryId) {
        studentService.addStudent(student, enquiryId);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Student added successfully", LocalDateTime.now()));
    }

    // 4. Update Student
    @PutMapping("/update/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable int id, @RequestBody Student student) {
        Student updatedStudent = studentService.updateStudent(student, id);
        return ResponseEntity.ok(updatedStudent);
    }

    // 5. Delete Student
    @DeleteMapping("/delete/{studentId}")
    public ResponseEntity<ApiResponse> deleteStudent(@PathVariable int studentId) {
        studentService.deleteStudent(studentId);
        
        return new ResponseEntity<>(new ApiResponse("Student record deleted", LocalDateTime.now()), HttpStatus.OK);
    }

    // 6. Filter by Batch
    @GetMapping("/getbybatch/{batchid}")
    public ResponseEntity<List<Student>> getByBatch(@PathVariable int batchid) {
        List<Student> students = studentService.getbybatch(batchid);
        return ResponseEntity.ok(students);
    }
}