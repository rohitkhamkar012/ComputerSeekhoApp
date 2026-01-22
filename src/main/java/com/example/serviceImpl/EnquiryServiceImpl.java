package com.example.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entities.ClosureReason;
import com.example.entities.Enquiry;
import com.example.entities.Staff;
import com.example.repositories.EnquiryRepository;
import com.example.repositories.StaffRepository;
import com.example.services.ClosureReasonService;
import com.example.services.EnquiryService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EnquiryServiceImpl implements EnquiryService {

    @Autowired
    private EnquiryRepository enquiryRepository;
    @Autowired
    private StaffRepository staffRepository;


    @Autowired
    private ClosureReasonService closureReasonService;

  

    @Override
    public Enquiry updateEnquiry(int enquiryId, Enquiry enquiry) {
        Optional<Enquiry> existing = enquiryRepository.findById(enquiryId);

        if (existing.isPresent()) {
            enquiry.setEnquiryId(enquiryId);
            return enquiryRepository.save(enquiry);
        }

        throw new RuntimeException("Enquiry not found with ID: " + enquiryId);
    }
    @Override
    public Enquiry createEnquiry(Enquiry enquiry) {

        // 🔹 Get staffId from request
        int staffId = enquiry.getStaff().getStaffId();

        // 🔹 Load staff from DB (managed entity)
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found with ID: " + staffId));

        // 🔹 Attach managed staff
        enquiry.setStaff(staff);

        // 🔹 Safety defaults
        enquiry.setEnquiryCounter(0);
        enquiry.setEnquiryIsActive(true);

        return enquiryRepository.save(enquiry);
    }


    @Override
    public void deleteEnquiry(int enquiryId) {
        Enquiry enquiry = enquiryRepository.findById(enquiryId)
                .orElseThrow(() -> new RuntimeException("Enquiry not found"));

        // ✅ Soft delete (business logic preserved)
        enquiry.setEnquiryIsActive(false);
        enquiryRepository.save(enquiry);
    }

    @Override
    public Enquiry getEnquiryById(int enquiryId) {
        return enquiryRepository.findById(enquiryId)
                .orElseThrow(() -> new RuntimeException("Enquiry not found with ID: " + enquiryId));
    }

    @Override
    public List<Enquiry> getAllEnquiries() {
        return enquiryRepository.findAll();
    }

    @Override
    public List<Enquiry> getEnquiryByStaff(String staffUsername) {
        return enquiryRepository.getByStaffList(staffUsername);
    }

    @Override
    public int updateMessage(int enquiryId, String message) {
        Optional<Enquiry> enquiry = enquiryRepository.findById(enquiryId);

        if (enquiry.isPresent()) {
            return enquiryRepository.updateMessage(enquiryId, message);
        }

        throw new RuntimeException("Enquiry not found with ID: " + enquiryId);
    }

    @Override
    public void deactivateEnquiry(int enquiryId, String message) {
        Enquiry enquiry = enquiryRepository.findById(enquiryId)
                .orElseThrow(() -> new RuntimeException("Enquiry not found"));

        // ✅ Your business logic preserved
        closureReasonService.addClosureReason(
                new ClosureReason(message, enquiry.getEnquirerName())
        );

        enquiry.setEnquiryIsActive(false);
        enquiryRepository.save(enquiry);
    }
}
