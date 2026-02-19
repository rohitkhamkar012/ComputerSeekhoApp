package com.example.services;

import java.util.List;

import com.example.DTO.PaymentDTO;
import com.example.entities.Payment;

public interface PaymentService {
    Payment getPaymentById(int id);

    List<Payment> getAllPayments();

    Payment savePayment(Payment payment);

    // Payment updatePayment(Payment payment);
    boolean existsByStudentId(int studentId);

    // void deletePayment(int paymentId);
    PaymentDTO getPaymentDTOById(int paymentId);

    List<Payment> getPaymentsByStudentId(int studentId);
}