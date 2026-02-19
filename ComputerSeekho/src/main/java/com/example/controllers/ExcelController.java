package com.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.DTO.ApiResponse;
import com.example.services.ExcelService;
import com.example.utils.ExcelHelper;

@RestController
@RequestMapping("/api/excel")
@CrossOrigin("*")
public class ExcelController {

    @Autowired
    ExcelService excelService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> uploadFile(@RequestParam("file") MultipartFile file,
            @RequestParam("type") String type) {
        String message = "";

        if (ExcelHelper.hasExcelFormat(file)) {
            try {
                excelService.save(file, type);
                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ApiResponse(message, java.time.LocalDateTime.now()));
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                        .body(new ApiResponse(message + " " + e.getMessage(), java.time.LocalDateTime.now()));
            }
        }

        message = "Please upload an excel file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(message, java.time.LocalDateTime.now()));
    }

    @GetMapping("/download")
    public ResponseEntity<org.springframework.core.io.InputStreamResource> getFile() {
        String filename = "sample_enquiries.xlsx";
        org.springframework.core.io.InputStreamResource file = new org.springframework.core.io.InputStreamResource(
                ExcelHelper.enquiriesToExcel());

        return ResponseEntity.ok()
                .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(org.springframework.http.MediaType
                        .parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(file);
    }
}
