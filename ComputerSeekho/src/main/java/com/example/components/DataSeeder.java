package com.example.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.example.entities.PaymentType;
import com.example.repositories.PaymentTypeRepository;

import java.util.Arrays;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    PaymentTypeRepository paymentTypeRepository;

    @Override
    public void run(String... args) throws Exception {
        seedPaymentTypes();
    }

    private void seedPaymentTypes() {
        if (paymentTypeRepository.count() == 0) {
            List<String> types = Arrays.asList("Cash", "Cheque", "Online Transfer", "UPI", "Bank Transfer");
            for (String desc : types) {
                PaymentType pt = new PaymentType();
                pt.setPaymentTypeDesc(desc);
                paymentTypeRepository.save(pt);
            }
            System.out.println("Seeded Payment Types");
        }
    }
}
