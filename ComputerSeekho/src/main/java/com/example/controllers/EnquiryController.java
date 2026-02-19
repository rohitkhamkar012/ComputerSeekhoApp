package com.example.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.DTO.ApiResponse;
import com.example.entities.Enquiry;
import com.example.services.EnquiryService;

@RestController
@RequestMapping("/enquiries")
@CrossOrigin("*")
public class EnquiryController {

    private final EnquiryService enquiryService;
    private final com.example.services.StaffService staffService;

    // ✅ Constructor Injection
    public EnquiryController(EnquiryService enquiryService, com.example.services.StaffService staffService) {
        this.enquiryService = enquiryService;
        this.staffService = staffService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createEnquiry(@RequestBody Enquiry enquiry) {
        // Auto-assign Staff based on logged-in user
        String username = org.springframework.security.core.context.SecurityContextHolder.getContext()
                .getAuthentication().getName();
        if (username != null && !username.equals("anonymousUser")) {
            com.example.entities.Staff staff = staffService.findByStaffUsername(username);
            if (staff != null) {
                enquiry.setStaff(staff);
            }
        } else {
            // Default to "admin" for public enquiries
            com.example.entities.Staff adminStaff = staffService.findByStaffUsername("admin");
            if (adminStaff != null) {
                enquiry.setStaff(adminStaff);
            }
        }

        enquiryService.createEnquiry(enquiry);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse("Enquiry Added", LocalDateTime.now()));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Enquiry>> getAllEnquiries() {
        return ResponseEntity.ok(enquiryService.getAllEnquiries());
    }

    @GetMapping("/getid/{id}")
    public ResponseEntity<?> getEnquiryById(@PathVariable int id) {
        Enquiry enquiry = enquiryService.getEnquiryById(id);
        if (enquiry == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Enquiry not found", LocalDateTime.now()));
        }
        return ResponseEntity.ok(enquiry);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateEnquiry(
            @PathVariable int id,
            @RequestBody Enquiry enquiry) {

        Enquiry existing = enquiryService.getEnquiryById(id);
        if (existing == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Enquiry not found", LocalDateTime.now()));
        }

        enquiryService.updateEnquiry(id, enquiry);
        return ResponseEntity.ok(
                new ApiResponse("Enquiry updated successfully", LocalDateTime.now()));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteEnquiry(@PathVariable int id) {
        enquiryService.deleteEnquiry(id);
        return ResponseEntity.ok(
                new ApiResponse("Enquiry deleted successfully", LocalDateTime.now()));
    }

    @GetMapping("/getbystaff/{staffUsername}")
    public ResponseEntity<List<Enquiry>> getEnquiryByStaff(
            @PathVariable String staffUsername) {

        return ResponseEntity.ok(
                enquiryService.getEnquiryByStaff(staffUsername));
    }

    @PutMapping("/updateMessage/{id}")
    public ResponseEntity<ApiResponse> updateMessage(
            @PathVariable int id,
            @RequestBody String message) {

        int updated = enquiryService.updateMessage(id, message);
        if (updated > 0) {
            return ResponseEntity.ok(
                    new ApiResponse("Message updated successfully", LocalDateTime.now()));
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("Message not updated", LocalDateTime.now()));
    }

    @PutMapping("/deactivate/{id}")
    public ResponseEntity<ApiResponse> deactivateEnquiry(
            @PathVariable int id,
            @RequestBody String message) {

        enquiryService.deactivateEnquiry(id, message);
        return ResponseEntity.ok(
                new ApiResponse("Deactivated Successfully", LocalDateTime.now()));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchEnquiry(@RequestParam String mobile) {
        Enquiry enquiry = enquiryService.getEnquiryByMobile(mobile);
        if (enquiry != null) {
            return ResponseEntity.ok(enquiry);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse("Enquiry not found", LocalDateTime.now()));
    }
}
