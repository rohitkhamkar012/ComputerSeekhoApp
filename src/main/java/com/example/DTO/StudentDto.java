package com.example.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {
    public StudentDto(int studentId, String photoUrl2, String studentName, String studentMobile, String courseName,
			String batchName, int paymentDue) {
		// TODO Auto-generated constructor stub
	}
	private int id;
    private String photoUrl;
    private String name; 
    private String mobile;
    private String course;
    public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPhotoUrl() {
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	public int getPendingFees() {
		return pendingFees;
	}
	public void setPendingFees(int pendingFees) {
		this.pendingFees = pendingFees;
	}
	private String batch;
    private int pendingFees;
}