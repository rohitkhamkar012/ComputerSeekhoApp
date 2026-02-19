package com.example.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entities.Recruiter;
import com.example.repositories.RecruiterRepository;

@Service
public class RecruiterService {

    @Autowired
    private RecruiterRepository recruiterRepository;

    // Add new recruiter
    public Recruiter addRecruiter(Recruiter recruiter) {
        return recruiterRepository.save(recruiter);
    }

    // Get all recruiters
    public List<Recruiter> getAllRecruiter() {
        return recruiterRepository.findAll();
    }

    // Delete recruiter by ID
    public boolean deleteRecruiter(int id) {
        if (recruiterRepository.existsById(id)) {
            recruiterRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Find recruiter by ID
    public Optional<Recruiter> getRecruiterById(int id) {
        return recruiterRepository.findById(id);
    }
}
