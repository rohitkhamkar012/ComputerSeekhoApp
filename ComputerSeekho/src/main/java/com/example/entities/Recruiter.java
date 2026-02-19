package com.example.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Entity
@Table(name = "recruiter")
@Data
public class Recruiter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruiter_id")
    private int recruiterId;

    @Column(name = "recruiter_name")
    @NotBlank(message = "Recruiter name is mandatory")
    private String recruiterName;

    @Column(name = "recruiter_location")
    @NotBlank(message = "Recruiter location is mandatory")
    private String recruiterLocation;

    @Column(name = "recruiter_image")
    @NotBlank(message = "add recruiter image")
    private String recruiterPhotoUrl;
}