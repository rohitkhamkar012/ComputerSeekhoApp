package com.example.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.DTO.ApiResponse;
import com.example.entities.Recruiter;
import com.example.services.RecruiterService;

@RestController
@RequestMapping("/recruiter")
@CrossOrigin(origins = "*") 
public class RecruiterController {

    @Autowired
    private RecruiterService recruiterService;

    // 1. ADD RECRUITER
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addRecruiter(@RequestBody Recruiter recruiter) {
        System.out.println("CONSOLE LOG: [POST] Adding new Recruiter: " + recruiter.getRecruiterName());
        recruiterService.addRecruiter(recruiter);
        return new ResponseEntity<>(new ApiResponse("Recruiter added successfully", LocalDateTime.now()), HttpStatus.CREATED);
    }

    // 2. GET ALL RECRUITERS
    @GetMapping("/getAll")
    public ResponseEntity<List<Recruiter>> getAllRecruiter() {
        System.out.println("CONSOLE LOG: [GET ALL] Fetching all recruiters from database");
        List<Recruiter> recruiters = recruiterService.getAllRecruiter();
        return ResponseEntity.ok(recruiters);
    }

    // 3. GET RECRUITER BY ID
    @GetMapping("/get/{id}")
    public ResponseEntity<Recruiter> getRecruiterById(@PathVariable int id) {
        System.out.println("CONSOLE LOG: [GET] Fetching Recruiter ID: " + id);
        Optional<Recruiter> recruiter = recruiterService.getRecruiterById(id);
        return recruiter.map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // 4. UPDATE RECRUITER
    @PutMapping("/update/{id}")
    public ResponseEntity<Recruiter> updateRecruiter(@PathVariable int id, @RequestBody Recruiter recruiter) {
        System.out.println("CONSOLE LOG: [PUT] Request to update Recruiter ID: " + id);
        Recruiter updatedRecruiter = recruiterService.updateRecruiter(recruiter, id);
        System.out.println("CONSOLE LOG: [SUCCESS] Recruiter updated: " + updatedRecruiter.getRecruiterName());
        return ResponseEntity.ok(updatedRecruiter);
    }

    // 5. DELETE RECRUITER
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteRecruiter(@PathVariable int id) {
        System.out.println("CONSOLE LOG: [DELETE] Removing Recruiter ID: " + id);
        recruiterService.deleteRecruiter(id);
        System.out.println("CONSOLE LOG: [SUCCESS] Recruiter ID " + id + " has been removed.");
        return ResponseEntity.ok(new ApiResponse("Recruiter Deleted successfully", LocalDateTime.now()));
    }
}