package com.example.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.DTO.PaymentDTO;
import com.example.entities.Payment;

import jakarta.transaction.Transactional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    boolean existsByStudentStudentId(int studentId);

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE student
        SET payment_due = payment_due - :amount
        WHERE student_id = :studentId
    """, nativeQuery = true)
    void updatePaymentDue(
            @Param("studentId") int studentId,
            @Param("amount") double amount
    );

    // ✅ FIXED JPQL (paymentTypeId)
    @Query("""
        SELECT new com.example.DTO.PaymentDTO(
            s.studentName,
            s.studentEmail,
            p.amount,
            p.paymentDate,
            pt.paymentTypeDesc
        )
        FROM Payment p
        JOIN p.student s
        JOIN p.paymentTypeId pt
        WHERE p.paymentId = :paymentId
    """)
    Optional<PaymentDTO> getPaymentOptional(
            @Param("paymentId") int paymentId
    );
}
