package com.example.services;
import java.util.List;

import com.example.entities.PaymentType;


public interface PaymentTypeService {
    List<PaymentType> getAllPayments();
    PaymentType getPaymentById(Integer id);
    PaymentType addPaymentType(PaymentType payment);
}