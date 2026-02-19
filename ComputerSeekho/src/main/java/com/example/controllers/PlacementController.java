package com.example.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.DTO.ApiResponse;
import com.example.DTO.PlacedStudentDTO;
import com.example.entities.Placement;
import com.example.services.PlacementService;

@RestController
@RequestMapping("/placement")
@CrossOrigin("*")
public class PlacementController {

    @Autowired
    private PlacementService placementService;

    @GetMapping("/all")
    public ResponseEntity<List<Placement>> getAllPlacements() {
        List<Placement> placements = placementService.getAllPlacements();
        if (placements == null) {
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.status(200).body(placements);
    }

    @GetMapping("/getByBatch/{batchId}")
    public ResponseEntity<List<PlacedStudentDTO>> getPlacedStudents(@PathVariable int batchId) {
        List<PlacedStudentDTO> placedStudentDTOs = placementService.getPlacedStudentByBatchId(batchId);
        System.out.println(placedStudentDTOs);
        if (placedStudentDTOs == null) {
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.status(200).body(placedStudentDTOs);
    }

    @GetMapping("/getByRecruiter/{recruiterId}")
    public ResponseEntity<List<PlacedStudentDTO>> getPlacedStudentsByRecruiter(@PathVariable int recruiterId) {
        List<PlacedStudentDTO> list = placementService.getPlacedStudentsByRecruiterId(recruiterId);
        if (list == null || list.isEmpty()) {
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.status(200).body(list);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addPlacement(@RequestBody Placement placement) {
        placementService.createPlacement(placement);
        // if(placement2 == null) {
        // return ResponseEntity.status(404).body(null);
        // }
        return new ResponseEntity<>(new ApiResponse("Placement Successfull", LocalDateTime.now()), HttpStatus.OK);
    }
}