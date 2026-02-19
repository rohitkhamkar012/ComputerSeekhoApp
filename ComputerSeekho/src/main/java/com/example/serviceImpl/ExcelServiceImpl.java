package com.example.serviceImpl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.entities.Enquiry;
import com.example.repositories.EnquiryRepository;
import com.example.services.ExcelService;
import com.example.utils.ExcelHelper;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    com.example.services.StaffService staffService;

    @Autowired
    EnquiryRepository enquiryRepository;

    @Override
    public void save(MultipartFile file, String entityType) {
        try {
            if ("enquiry".equalsIgnoreCase(entityType)) {
                // Get Logged in Staff
                String username = org.springframework.security.core.context.SecurityContextHolder.getContext()
                        .getAuthentication().getName();
                com.example.entities.Staff staff = null;
                if (username != null && !username.equals("anonymousUser")) {
                    staff = staffService.findByStaffUsername(username);
                }

                List<Enquiry> enquiries = ExcelHelper.excelToEnquiries(file.getInputStream(), staff);
                enquiryRepository.saveAll(enquiries);
            }
            // Add other types here if needed
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }

    @Override
    public List<Enquiry> getAllEnquiries() {
        return enquiryRepository.findAll();
    }
}
