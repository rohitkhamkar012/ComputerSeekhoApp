package com.example.repositories;

import com.example.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface StudentRepositories extends JpaRepository<Student, Integer> {
    
    @Query(value = "SELECT * FROM student WHERE course_id = ?1", nativeQuery = true)
    List<Student> findbycourse(int courseId);

    @Query(value = "SELECT * FROM student WHERE batch_id = ?1", nativeQuery = true)
    List<Student> findByBatchId(int batchId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE student s SET s.payment_due = (SELECT c.course_fee FROM course c WHERE c.course_id = s.course_id) WHERE s.student_id = ?1", nativeQuery = true)
    void updatePayment(int studentId);
}