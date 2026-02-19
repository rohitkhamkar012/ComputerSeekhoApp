package com.example.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.DTO.ApiResponse;
import com.example.entities.Staff;
import com.example.services.StaffService;

@RestController
@RequestMapping("/staff")
@CrossOrigin("*")
public class StaffController {

    @Autowired
    private StaffService staffService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> saveStaff(@RequestBody Staff staff) {
        Staff isAdded = staffService.saveStaff(staff);
        if (isAdded != null) {
            return new ResponseEntity<>(new ApiResponse("Staff added successfully", LocalDateTime.now()),
                    HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new ApiResponse("Staff not added", LocalDateTime.now()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateStaff(@RequestBody Staff staff) {
        boolean isUpdated = staffService.updateStaff(staff);
        if (isUpdated) {
            return new ResponseEntity<>(new ApiResponse("Staff updated successfully", LocalDateTime.now()),
                    HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse("Staff not found", LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @GetMapping("/get/{staffUsername}")
    public ResponseEntity<?> getStaff(@PathVariable String staffUsername) {
        Staff staff = staffService.findByStaffUsername(staffUsername);
        if (staff != null) {
            return new ResponseEntity<>(staff, HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse("Staff not found", LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete/{staffId}")
    public ResponseEntity<ApiResponse> deleteStaff(@PathVariable int staffId) {
        boolean isDeleted = staffService.deleteByStaffId(staffId);
        if (isDeleted) {
            return new ResponseEntity<>(new ApiResponse("Staff deleted successfully", LocalDateTime.now()),
                    HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse("Staff not found", LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Staff>> getAllStaff() {
        List<Staff> staffList = staffService.getAllStaff();
        return new ResponseEntity<>(staffList, HttpStatus.OK);
    }

    @GetMapping("/username/{staffUsername}")
    public ResponseEntity<ApiResponse> getStaffIdByStaffUsername(@PathVariable String staffUsername) {
        int id = staffService.getStaffIdByStaffUsername(staffUsername);
        if (id > 0) {
            return new ResponseEntity<>(new ApiResponse(String.valueOf(id), LocalDateTime.now()), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse("Staff not found", LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }
}
