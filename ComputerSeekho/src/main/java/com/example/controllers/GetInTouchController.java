package com.example.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.DTO.ApiResponse;
import com.example.entities.GetInTouch;
import com.example.services.GetInTouchService;

@RestController
@RequestMapping("/getInTouch")
@org.springframework.web.bind.annotation.CrossOrigin("*")
public class GetInTouchController {

    @Autowired
    private GetInTouchService getInTouchService;

    @GetMapping("/getAll")
    public ResponseEntity<List<GetInTouch>> getAllGetInTouch() {
        return ResponseEntity.ok(getInTouchService.getAll());
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addTemporaryEnquiry(@RequestBody GetInTouch getInTouch) {
        getInTouchService.save(getInTouch);
        return ResponseEntity.ok(new ApiResponse("Added successfully", LocalDateTime.now()));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteGetinTouch(@PathVariable int id) {
        getInTouchService.deleteById(id);
        return ResponseEntity.ok(new ApiResponse("Enquiry deleted successfully", LocalDateTime.now()));
    }
}