package com.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.example.DTO.AuthRequest;
import com.example.filter.JWTokenGenerationFilter;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private com.example.services.StaffService staffService;

    @Autowired
    private JWTokenGenerationFilter jwtFilter;

    @PostMapping("/signIn")
    public ResponseEntity<String> signIn(@RequestBody AuthRequest authRequest, HttpServletResponse response) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(auth);
        jwtFilter.generateToken(auth, response);

        return ResponseEntity.ok("Login successful. JWT sent in Authorization header.");
    }

    @PostMapping("/google-login")
    public ResponseEntity<?> googleLogin(@RequestBody java.util.Map<String, String> body,
            HttpServletResponse response) {
        String token = body.get("token");
        try {
            // Verify Token with Google
            java.net.URL url = new java.net.URL("https://oauth2.googleapis.com/tokeninfo?id_token=" + token);
            java.net.HttpURLConnection con = (java.net.HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            if (con.getResponseCode() != 200) {
                return ResponseEntity.status(401).body("Invalid Google Token");
            }

            // Parse Response to get Email
            // Parse Response to get Email using ObjectMapper (Safer)
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.JsonNode rootNode = mapper.readTree(con.getInputStream());

            String email = "";
            if (rootNode.has("email")) {
                email = rootNode.get("email").asText().trim();

            }

            System.out.println("Google Login Attempt: " + email); // DEBUG

            // Check if Staff exists with this email
            com.example.entities.Staff staff = staffService.getStaffByEmail(email);
            if (staff == null) {
                System.out.println("Staff not found for email: " + email); // DEBUG
                return ResponseEntity.status(401).body("Email not registered as Staff: " + email);
            }

            // Login Successful - Generate App Token
            Authentication auth = new UsernamePasswordAuthenticationToken(staff.getStaffUsername(), null,
                    org.springframework.security.core.authority.AuthorityUtils
                            .createAuthorityList(staff.getStaffRole()));

            SecurityContextHolder.getContext().setAuthentication(auth);
            jwtFilter.generateToken(auth, response);

            // Return Username for Frontend Context
            java.util.Map<String, String> responseBody = new java.util.HashMap<>();
            responseBody.put("message", "Google Login Successful");
            responseBody.put("username", staff.getStaffUsername());
            return ResponseEntity.ok(responseBody);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing Google Login");
        }
    }
}
