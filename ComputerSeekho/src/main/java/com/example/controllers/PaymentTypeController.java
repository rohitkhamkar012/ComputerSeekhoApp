package com.example.controllers;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.DTO.ApiResponse;
import com.example.entities.PaymentType;
import com.example.services.PaymentTypeService;

@RestController
@RequestMapping("/paymentTypes")
public class PaymentTypeController {

    @Autowired
    private PaymentTypeService paymentTypeService;


    @GetMapping("/getAll")
    public ResponseEntity<List<PaymentType>> getAllPaymentTypes() {
       return  new ResponseEntity<>( paymentTypeService.getAllPayments(),HttpStatus.OK);
    }

    // @GetMapping("/get")
    // public String getPayment() {
    //     return ResponseEntity.ok(paymentService.getPayment());
    // }
    
    @GetMapping("/{id}")
    public PaymentType getPaymentTypeById(@PathVariable Integer id) {
        return paymentTypeService.getPaymentById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addPaymentType(@RequestBody PaymentType payment) {
        paymentTypeService.addPaymentType(payment);
         return new ResponseEntity<>(new ApiResponse("Payment type added sucessfully", LocalDateTime.now()),HttpStatus.OK);

    }
}