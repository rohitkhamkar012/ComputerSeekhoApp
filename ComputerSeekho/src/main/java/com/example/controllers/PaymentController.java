package com.example.controllers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.client.RestTemplate;

import com.example.DTO.ApiResponse;
import com.example.DTO.PaymentDTO;
import com.example.entities.Payment;
import com.example.entities.Student;
import com.example.services.PaymentService;
import com.example.services.StudentService;

@RestController
@RequestMapping("/payment")
@CrossOrigin("*")
public class PaymentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private com.example.services.EmailService emailService;

    @Autowired
    private com.example.services.ReceiptService receiptService;

    @GetMapping("/get/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable int id) {
        Payment payment = paymentService.getPaymentById(id);
        if (payment != null) {
            return ResponseEntity.ok(payment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/getByStudent/{studentId}")
    public ResponseEntity<List<Payment>> getPaymentsByStudentId(@PathVariable int studentId) {
        return ResponseEntity.ok(paymentService.getPaymentsByStudentId(studentId));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createPayment(@RequestBody Payment payment) {
        Student student = studentService.getStudentById(payment.getStudent().getStudentId()).get();

        // Validate Amount
        if (student.getPaymentDue() - payment.getAmount() < 0) {
            return new ResponseEntity<>(new ApiResponse("Payment Amount Exceeds Due", LocalDateTime.now()),
                    HttpStatus.NOT_ACCEPTABLE);
        }

        // Save Payment
        Payment savedPayment = paymentService.savePayment(payment);

        // Generate Receipt
        try {
            com.example.entities.Receipt receipt = new com.example.entities.Receipt();
            receipt.setPaymentId(savedPayment);
            receipt.setReceiptDate(savedPayment.getPaymentDate());
            receipt.setReceiptAmount(savedPayment.getAmount());
            receiptService.addReceipt(receipt);
        } catch (Exception e) {
            System.err.println("Error generating receipt: " + e.getMessage());
        }

        // Send Email
        try {
            String subject = "Payment Receipt - Computer Seekho";
            String body = "Dear " + student.getStudentName() + ",\n\n" +
                    "We have received a payment of Rs. " + savedPayment.getAmount() + ".\n" +
                    "Date: " + savedPayment.getPaymentDate() + "\n" +
                    "Transaction ID: " + savedPayment.getPaymentId() + "\n\n" +
                    "Thank you,\nComputer Seekho Accounts";

            emailService.sendSimpleEmail(student.getStudentEmail(), subject, body);
        } catch (Exception e) {
            System.err.println("Error sending payment email: " + e.getMessage());
        }

        return new ResponseEntity<>(new ApiResponse("Payment Successful", LocalDateTime.now()), HttpStatus.CREATED);
    }
}

// @PutMapping("/{id}")
// public ResponseEntity<Payment> updatePayment(@PathVariable int id,
// @RequestBody Payment paymentDetails) {
// Payment payment = paymentService.getPaymentById(id);
// if (payment != null) {
// payment.setPaymentTypeId(paymentDetails.getPaymentTypeId());
// payment.setPaymentDate(paymentDetails.getPaymentDate());
// payment.setStudent(paymentDetails.getStudent());
// payment.setAmount(paymentDetails.getAmount());
// Payment updatedPayment = paymentService.updatePayment(payment);
// return ResponseEntity.ok(updatedPayment);
// } else {
// return ResponseEntity.notFound().build();
// }
// }

// @DeleteMapping("/{id}")
// public ResponseEntity<Void> deletePayment(@PathVariable int id) {
// Payment payment = paymentService.getPaymentById(id);
// if (payment != null) {
// paymentService.deletePayment(id);
// return ResponseEntity.noContent().build();
// } else {
// return ResponseEntity.notFound().build();
// }
// }

// as we are not using update and delete payment methods in this project
// we have commented them out