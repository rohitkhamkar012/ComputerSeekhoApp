package com.example.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.entities.Staff;
import com.example.services.StaffService;

@Component
public class UsernamePasswordAuthentication implements AuthenticationProvider {

    private final StaffService service;
    private final PasswordEncoder passwordEncoder;

    public UsernamePasswordAuthentication(StaffService service, @Lazy PasswordEncoder passwordEncoder) {
        this.service = service;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        System.out.println("DEBUG: Authenticating user: " + username);
        if (service == null)
            System.out.println("DEBUG: FATAL - service is null");
        if (passwordEncoder == null)
            System.out.println("DEBUG: FATAL - passwordEncoder is null");

        Staff staff = service.findByStaffUsername(username);
        if (staff == null)
            throw new BadCredentialsException("User not found");

        if (!passwordEncoder.matches(password, staff.getStaffPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(staff.getStaffRole()));

        return new UsernamePasswordAuthenticationToken(username, password, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
