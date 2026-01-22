package com.example.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DTO.PaymentDTO;
import com.example.entities.Payment;
import com.example.repositories.PaymentRepository;


@Service
public class PaymentServiceImpl implements PaymentService {

	 @Autowired
	    private PaymentRepository paymentRepository;

	
	@Override
	public Payment getPaymentById(int id) {
		// TODO Auto-generated method stub
		 return paymentRepository.findById(id).orElse(null);
	}

	@Override
	public List<Payment> getAllPayments() {
		// TODO Auto-generated method stub
		return paymentRepository.findAll();
	}

	@Override
	public Payment savePayment(Payment payment) {
		// TODO Auto-generated method stub
		 Payment payment2 = paymentRepository.save(payment);
	        System.out.println(payment2.getStudent().getPaymentDue());
	        paymentRepository.updatePaymentDue(payment.getStudent().getStudentId(), payment2.getAmount());
	        return payment2;
	}

	@Override
	public boolean existsByStudentId(int studentId) {
		// TODO Auto-generated method stub
		 return paymentRepository.existsByStudentStudentId(studentId);
	}

	@Override
	public PaymentDTO getPaymentDTOById(int paymentId) {
	    return paymentRepository.getPaymentOptional(paymentId)
	            .orElseThrow(() ->
	                    new RuntimeException("PaymentDTO not found for id: " + paymentId)
	            );
	}
	
//	@Override
//	public PaymentDTO getPaymentDTOById(int paymentId) {
//		  return paymentRepository.getPaymentOptional(paymentId).get();
//	}
 
}
