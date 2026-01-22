package com.example.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entities.PaymentType;
import com.example.repositories.PaymentTypeRepository;

@Service
public class PaymentTypeServiceImpl implements PaymentTypeService {

    @Autowired
    private PaymentTypeRepository paymentTypeRepository;

	@Override
	public List<PaymentType> getAllPayments() {
		// TODO Auto-generated method stub
		return paymentTypeRepository.findAll();
	}

	@Override
	public PaymentType getPaymentById(Integer id) {
		// TODO Auto-generated method stub
		Optional<PaymentType> payment = paymentTypeRepository.findById(id);
        return payment.orElse(null); 
	}

	@Override
	public PaymentType addPaymentType(PaymentType payment) {
		// TODO Auto-generated method stub
		 return paymentTypeRepository.save(payment);
	}

}