package com.example.services;

import java.util.List;
import java.util.Optional;

import com.example.DTO.StudentDto;
import com.example.entities.Student;

public interface StudentService {
    Optional<Student> getStudentById(int studentId);

    List<StudentDto> getAllStudents();

    Student addStudent(Student student, int enquiryId);

    Student updateStudent(Student student, int studentId);

    List<Student> getAllStudentsEntity();

    void deleteStudent(int studentId);

    List<Student> getbybatch(int batchId);

    List<Student> getbycourse(int courseId);

}