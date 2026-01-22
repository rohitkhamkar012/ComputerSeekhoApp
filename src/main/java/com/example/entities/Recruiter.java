package com.example.entities;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name ="recruiter")
@Data
public class Recruiter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruiter_id")  
    private int recruiterId;

    @Column(name = "recruiter_name")
    @NotBlank(message = "Recruiter name is mandatory")
    @Size(min = 3,max = 50 ,message="Recruiter name must be between 3 and 50")
    private String recruiterName;
    
    @Column(name = "recruiter_location")
    @NotBlank(message = "Recruiter location is mandatory")
    @Size(min = 3,max = 50,message = "Recruiter location must be between 3 and 50")
    private String recruiterLocation;

    @Column(name = "recruiter_image")
    @NotBlank(message = "add recruiter image")
    private String recruiterPhotoUrl;

	public int getRecruiterId() {
		return recruiterId;
	}

	public void setRecruiterId(int recruiterId) {
		this.recruiterId = recruiterId;
	}

	public String getRecruiterName() {
		return recruiterName;
	}

	public void setRecruiterName(String recruiterName) {
		this.recruiterName = recruiterName;
	}

	public String getRecruiterLocation() {
		return recruiterLocation;
	}

	public void setRecruiterLocation(String recruiterLocation) {
		this.recruiterLocation = recruiterLocation;
	}

	public String getRecruiterPhotoUrl() {
		return recruiterPhotoUrl;
	}

	public void setRecruiterPhotoUrl(String recruiterPhotoUrl) {
		this.recruiterPhotoUrl = recruiterPhotoUrl;
	}
    
    
}