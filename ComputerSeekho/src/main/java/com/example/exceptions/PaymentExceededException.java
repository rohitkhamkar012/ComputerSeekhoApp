package com.example.exceptions;
public class PaymentExceededException extends Exception {
    public PaymentExceededException(String message) {
        super(message);
    }
}