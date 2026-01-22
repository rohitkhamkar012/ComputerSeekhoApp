package com.example.DTO;

import lombok.Data;

@Data
public class StudentDto {
    private int id;
    private String photoUrl;
    private String name; 
    private String mobile;
    private String course;
    private String batch;
    private int pendingFees;
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
	public StudentDto(int id, String photoUrl, String name, String mobile, String course, String batch,
			int pendingFees) {
		super();
		this.id = id;
		this.photoUrl = photoUrl;
		this.name = name;
		this.mobile = mobile;
		this.course = course;
		this.batch = batch;
		this.pendingFees = pendingFees;
	}
	public StudentDto() {}
}