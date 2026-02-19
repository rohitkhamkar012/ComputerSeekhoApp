package com.example.serviceImpl;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entities.PaymentType;
import com.example.repositories.PaymentTypeRepository;
import com.example.services.PaymentTypeService;



@Service
public class PaymentTypeServiceImpl implements PaymentTypeService {

    @Autowired
    private PaymentTypeRepository paymentTypeRepository;

    @Override
    public List<PaymentType> getAllPayments() {
        return paymentTypeRepository.findAll();
    }

    @Override
    public PaymentType getPaymentById(Integer paymentId) {
        Optional<PaymentType> payment = paymentTypeRepository.findById(paymentId);
        return payment.orElse(null); 
    }

    @Override
    public PaymentType addPaymentType(PaymentType paymentType) {
        return paymentTypeRepository.save(paymentType);
    }
}