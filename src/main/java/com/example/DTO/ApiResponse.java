package com.example.DTO;

import java.time.LocalDateTime;

public class ApiResponse {

    private String message;
    private LocalDateTime timestamp;

    // ✅ REQUIRED: No-args constructor
    public ApiResponse() {
    }

    // ✅ Constructor used in controller
    public ApiResponse(String message, LocalDateTime timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    // ✅ GETTERS (MANDATORY for JSON)
    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    // (Optional setters)
    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
