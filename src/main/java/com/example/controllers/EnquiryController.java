package com.example.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.DTO.ApiResponse;
import com.example.entities.Enquiry;
import com.example.services.EnquiryService;

@RestController
@RequestMapping("/enquiries")
public class EnquiryController {

    @Autowired
    private EnquiryService enquiryService;

    // 1️⃣ Create enquiry
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createEnquiry(@RequestBody Enquiry enquiry) {
        enquiryService.createEnquiry(enquiry);
        return new ResponseEntity<>(
                new ApiResponse("Enquiry Added", LocalDateTime.now()),
                HttpStatus.CREATED
        );
    }

    // 2️⃣ Get all enquiries
    @GetMapping("/getAll")
    public ResponseEntity<List<Enquiry>> getAllEnquiries() {
        List<Enquiry> enquiries = enquiryService.getAllEnquiries();

        if (enquiries == null || enquiries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(enquiries);
    }

    // 3️⃣ Get enquiry by ID
    @GetMapping("/getid/{id}")
    public ResponseEntity<Enquiry> getEnquiryById(@PathVariable int id) {
        return ResponseEntity.ok(enquiryService.getEnquiryById(id));
    }

    // 4️⃣ Update enquiry
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateEnquiry(
            @PathVariable int id,
            @RequestBody Enquiry enquiry) {

        enquiryService.updateEnquiry(id, enquiry);

        return ResponseEntity.ok(
                new ApiResponse("Enquiry updated successfully", LocalDateTime.now())
        );
    }

    // 5️⃣ Delete enquiry (soft delete)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteEnquiry(@PathVariable int id) {
        enquiryService.deleteEnquiry(id);
        return ResponseEntity.ok(
                new ApiResponse("Enquiry deleted successfully", LocalDateTime.now())
        );
    }

    // 6️⃣ Get enquiries by staff
    @GetMapping("/getbystaff/{staffUsername}")
    public ResponseEntity<List<Enquiry>> getEnquiryByStaff(
            @PathVariable String staffUsername) {

        List<Enquiry> enquiries = enquiryService.getEnquiryByStaff(staffUsername);

        if (enquiries == null || enquiries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(enquiries);
    }

    // 7️⃣ Update enquiry message
    @PutMapping("/updateMessage/{id}")
    public ResponseEntity<ApiResponse> updateMessage(
            @PathVariable int id,
            @RequestBody String message) {

        int updated = enquiryService.updateMessage(id, message);

        if (updated > 0) {
            return ResponseEntity.ok(
                    new ApiResponse("Message updated successfully", LocalDateTime.now())
            );
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("Message not updated", LocalDateTime.now()));
    }

    // 8️⃣ Deactivate enquiry
    @PutMapping("/deactivate/{id}")
    public ResponseEntity<ApiResponse> deactivateEnquiry(
            @PathVariable int id,
            @RequestBody String message) {

        enquiryService.deactivateEnquiry(id, message);
        return ResponseEntity.ok(
                new ApiResponse("Deactivated successfully", LocalDateTime.now())
        );
    }
}
