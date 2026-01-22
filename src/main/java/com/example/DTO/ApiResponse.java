package com.example.DTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse {
    
    private String message;
    private LocalDateTime timestamp;
    public ApiResponse(String message, LocalDateTime timestamp)
    {
    	this.message=message;
    	this.timestamp=timestamp;
    }

}