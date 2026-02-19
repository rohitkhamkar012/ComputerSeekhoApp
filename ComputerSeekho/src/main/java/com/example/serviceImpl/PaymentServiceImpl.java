package com.example.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DTO.PaymentDTO;
import com.example.entities.Payment;
import com.example.repositories.PaymentRepository;
import com.example.services.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment getPaymentById(int paymentId) {
        return paymentRepository.findById(paymentId).orElse(null);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public Payment savePayment(Payment payment) {
        Payment payment2 = paymentRepository.save(payment);
        System.out.println(payment2.getStudent().getPaymentDue());
        paymentRepository.updatePaymentDue(payment.getStudent().getStudentId(), payment2.getAmount());
        return payment2;
    }

    // @Override
    // public Payment updatePayment(Payment payment) {
    // return paymentRepository.save(payment);
    // }

    // @Override
    // public void deletePayment(int paymentId) {
    // paymentRepository.deleteById(paymentId);
    // }

    @Override
    public boolean existsByStudentId(int studentId) {
        return paymentRepository.existsByStudentStudentId(studentId);
    }

    @Override
    public PaymentDTO getPaymentDTOById(int paymentId) {
        return paymentRepository.getPaymentOptional(paymentId).get();
    }

    @Override
    public List<Payment> getPaymentsByStudentId(int studentId) {
        return paymentRepository.findByStudent_StudentId(studentId);
    }
}