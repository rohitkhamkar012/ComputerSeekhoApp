package com.example.services;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.example.entities.Enquiry;

public interface ExcelService {
    void save(MultipartFile file, String entityType);

    List<Enquiry> getAllEnquiries();
}
