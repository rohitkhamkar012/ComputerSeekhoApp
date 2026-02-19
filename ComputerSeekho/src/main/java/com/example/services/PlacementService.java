package com.example.services;

import java.util.List;

import com.example.DTO.PlacedStudentDTO;
import com.example.entities.Placement;

public interface PlacementService {
    Placement createPlacement(Placement placement);

    List<Placement> getAllPlacements();

    List<PlacedStudentDTO> getPlacedStudentByBatchId(int batchId);

    List<PlacedStudentDTO> getPlacedStudentsByRecruiterId(int recruiterId);
}