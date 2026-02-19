package com.example.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Data
@Table(name = "staff")
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staff_id")
    private int staffId;

    @Column(name = "staff_name")
    @NotBlank
    private String staffName;

    @Column(name = "photo_url")
    @NotBlank
    private String photoUrl;

    @Column(name = "staff_email")
    @NotBlank
    @Email
    private String staffEmail;

    @Column(name = "staff_mobile")
    @NotBlank
    private String staffMobile;

    @Column(name = "staff_gender")
    @NotBlank
    private String staffGender;

    @Column(name = "staff_username")
    @NotBlank
    @Size(min = 5, max = 30)
    private String staffUsername;

    @Column(name = "staff_password")
    @NotBlank
    @Size(min = 8)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String staffPassword;

    @Column(name = "staff_role")
    private String staffRole;
}
