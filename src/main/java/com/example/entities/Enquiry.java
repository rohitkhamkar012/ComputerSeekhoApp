package com.example.entities;
import java.time.LocalDate;
import jakarta.persistence.*;
//import lombok.Data;

@Entity
@Table(name = "enquiry")
//@Data
public class Enquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enquiry_id")
    private int enquiryId;

    @Column(name = "enquirer_name")
    private String enquirerName;

    @Column(name = "enquirer_address")
    private String enquirerAddress;

    @Column(name = "enquirer_mobile")
    private String enquirerMobile;

    @Column(name = "enquirer_email_id")
    private String enquirerEmailId;

    @Column(name = "enquiry_date")
    private LocalDate enquiryDate;

    @Column(name = "enquirer_query")
    private String enquirerQuery;

    @Column(name = "course_name")
    private String courseName;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;

    @Column(name = "student_name")
    private String studentName;

    @Column(name = "enquiry_counter")
    private Integer enquiryCounter;

    @Column(name = "closure_reason")
    private String closureReason;

    @Column(name = "follow_up_date")
    private LocalDate followUpDate;

    @Column(name = "enquiry_is_active", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean enquiryIsActive = true;

	public Integer getEnquiryId() {
		return enquiryId;
	}

	public void setEnquiryId(int enquiryId) {
		this.enquiryId = enquiryId;
	}

	public String getEnquirerName() {
		return enquirerName;
	}

	public void setEnquirerName(String enquirerName) {
		this.enquirerName = enquirerName;
	}

	public String getEnquirerAddress() {
		return enquirerAddress;
	}

	public void setEnquirerAddress(String enquirerAddress) {
		this.enquirerAddress = enquirerAddress;
	}

	public String getEnquirerMobile() {
		return enquirerMobile;
	}

	public void setEnquirerMobile(String enquirerMobile) {
		this.enquirerMobile = enquirerMobile;
	}

	public String getEnquirerEmailId() {
		return enquirerEmailId;
	}

	public void setEnquirerEmailId(String enquirerEmailId) {
		this.enquirerEmailId = enquirerEmailId;
	}

	public LocalDate getEnquiryDate() {
		return enquiryDate;
	}

	public void setEnquiryDate(LocalDate enquiryDate) {
		this.enquiryDate = enquiryDate;
	}

	public String getEnquirerQuery() {
		return enquirerQuery;
	}

	public void setEnquirerQuery(String enquirerQuery) {
		this.enquirerQuery = enquirerQuery;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public Integer getEnquiryCounter() {
		return enquiryCounter;
	}

	public void setEnquiryCounter(Integer enquiryCounter) {
		this.enquiryCounter = enquiryCounter;
	}

	public String getClosureReason() {
		return closureReason;
	}

	public void setClosureReason(String closureReason) {
		this.closureReason = closureReason;
	}

	public LocalDate getFollowUpDate() {
		return followUpDate;
	}

	public void setFollowUpDate(LocalDate followUpDate) {
		this.followUpDate = followUpDate;
	}

	public boolean isEnquiryIsActive() {
		return enquiryIsActive;
	}

	public void setEnquiryIsActive(boolean enquiryIsActive) {
		this.enquiryIsActive = enquiryIsActive;
	}
	@PrePersist
	public void prePersist() {
	    if (enquiryCounter == null) {
	        enquiryCounter = 0;
	    }
	}

}