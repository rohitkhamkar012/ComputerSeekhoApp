package com.example.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.DTO.ApiResponse;
import com.example.entities.Recruiter;
import com.example.services.RecruiterService;

@RestController
@RequestMapping("/recruiter")
@CrossOrigin("*")
public class RecruiterController {

    @Autowired
    private RecruiterService recruiterService;

    // Add recruiter
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addRecruiter(@RequestBody Recruiter recruiter) {
        Recruiter savedRecruiter = recruiterService.addRecruiter(recruiter);
        if (savedRecruiter != null) {
            return new ResponseEntity<>(new ApiResponse("Recruiter added successfully", LocalDateTime.now()),
                    HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new ApiResponse("Failed to add recruiter", LocalDateTime.now()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Get all recruiters
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllRecruiter() {
        List<Recruiter> recruiters = recruiterService.getAllRecruiter();
        if (recruiters.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("No recruiters found", LocalDateTime.now()),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(recruiters, HttpStatus.OK);
    }

    // Delete recruiter by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteRecruiter(@PathVariable int id) {
        boolean isDeleted = recruiterService.deleteRecruiter(id);
        if (isDeleted) {
            return new ResponseEntity<>(new ApiResponse("Recruiter deleted successfully", LocalDateTime.now()),
                    HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse("Recruiter not found", LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    // Optional: Get recruiter by ID
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getRecruiterById(@PathVariable int id) {
        return recruiterService.getRecruiterById(id)
                .<ResponseEntity<?>>map(recruiter -> new ResponseEntity<>(recruiter, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(new ApiResponse("Recruiter not found", LocalDateTime.now()),
                        HttpStatus.NOT_FOUND));
    }
}
