package com.example.DTO;
import java.time.LocalDate;

import lombok.Data;

@Data
public class PaymentDTO {
    private String studentName;
    private String studentEmail;
    private int amount;
    private LocalDate paymentDate;
    private String paymentTypeDesc;

    public PaymentDTO(String studentName, String studentEmail, int amount, LocalDate paymentDate, String paymentTypeDesc) {
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentTypeDesc = paymentTypeDesc;
    }

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getStudentEmail() {
		return studentEmail;
	}

	public void setStudentEmail(String studentEmail) {
		this.studentEmail = studentEmail;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public LocalDate getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDate paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getPaymentTypeDesc() {
		return paymentTypeDesc;
	}

	public void setPaymentTypeDesc(String paymentTypeDesc) {
		this.paymentTypeDesc = paymentTypeDesc;
	}

    // // Getters & Setters
    // public String getStudentName() { return studentName; }
    // public String getStudentEmail() { return studentEmail; }
    // public int getAmount() { return amount; }
    // public LocalDate getPaymentDate() { return paymentDate; }
    // public String getPaymentTypeDesc() { return paymentTypeDesc; }
}