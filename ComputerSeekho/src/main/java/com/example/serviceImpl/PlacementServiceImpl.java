package com.example.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DTO.PlacedStudentDTO;
import com.example.entities.Placement;
import com.example.repositories.PlacementRepository;
import com.example.services.PlacementService;

@Service
public class PlacementServiceImpl implements PlacementService {

    @Autowired
    private com.example.repositories.StudentRepositories studentRepositories;

    @Autowired
    private PlacementRepository placementRepository;

    @Override
    public Placement createPlacement(Placement placement) {
        var student = studentRepositories.findById(placement.getStudentID().getStudentId()).orElse(null);

        return placementRepository.save(placement);
    }

    @Override
    public List<Placement> getAllPlacements() {
        return placementRepository.findAll();
    }

    @Override
    public List<PlacedStudentDTO> getPlacedStudentByBatchId(int batchId) {
        return placementRepository.findByBatchId(batchId);
    }

    @Override
    public List<PlacedStudentDTO> getPlacedStudentsByRecruiterId(int recruiterId) {
        return placementRepository.findByRecruiterId(recruiterId);
    }
}