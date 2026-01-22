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

    public Recruiter addRecruiter(Recruiter recruiter) {
        return recruiterRepository.save(recruiter);
    }

    public List<Recruiter> getAllRecruiter() {
        return recruiterRepository.findAll();
    }

    public Optional<Recruiter> getRecruiterById(int id) {
        return recruiterRepository.findById(id);
    }

    public Recruiter updateRecruiter(Recruiter recruiter, int id) {
        return recruiterRepository.findById(id).map(existing -> {
            recruiter.setRecruiterId(id); // Set the ID to perform an update
            return recruiterRepository.save(recruiter);
        }).orElseThrow(() -> new RuntimeException("Recruiter not found with id " + id));
    }

    public void deleteRecruiter(int id) {
        recruiterRepository.deleteById(id);
    }
}