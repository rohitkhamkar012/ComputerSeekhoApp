package com.example.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.entities.Staff;
import com.example.filter.JwtUtil;
import com.example.services.StaffService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private StaffService staffService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> data) {

        String username = data.get("username");
        String password = data.get("password");

        Staff staff = staffService.findByStaffUsername(username);

        if (staff == null) {
            throw new RuntimeException("invalid username or password");
        }

        boolean match = passwordEncoder.matches(
                password,
                staff.getStaffPassword()
        );

        if (!match) {
            throw new RuntimeException("invalid username or password");
        }

        String token = jwtUtil.generateToken(staff.getStaffUsername());

        return Map.of("token", token);
    }
}
