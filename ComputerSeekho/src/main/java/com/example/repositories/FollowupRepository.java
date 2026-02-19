package com.example.repositories;

import com.example.entities.Followup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowupRepository extends JpaRepository<Followup, Integer> {
    
    // Optional: Find all active followups
    List<Followup> findByIsActiveTrue();

    // Optional: Find all followups for a specific enquiry
    List<Followup> findByEnquiryEnquiryId(int enquiryId);

    // Optional: Find all followups assigned to a specific staff
    List<Followup> findByStaffStaffId(int staffId);
}
