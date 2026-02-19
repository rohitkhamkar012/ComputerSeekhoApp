package com.example.filter;

import java.util.*;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.example.configuration.SecurityConstant;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTokenGenerationFilter {

    public void generateToken(Authentication authentication, HttpServletResponse response) {
        SecretKey key = Keys.hmacShaKeyFor(SecurityConstant.JWT_KEY.getBytes(StandardCharsets.UTF_8));

        String jwt = Jwts.builder()
                .setIssuer("Computer Seekho")
                .setSubject("JWT Token")
                .claim("username", authentication.getName())
                .claim("authorities", getAuthorities(authentication.getAuthorities()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 30 * 60 * 1000)) // 30 min
                .signWith(key)
                .compact();

        response.setHeader(SecurityConstant.JWT_HEADER, "Bearer " + jwt);
    }

    private String getAuthorities(Collection<? extends GrantedAuthority> authorities) {
        List<String> authList = new ArrayList<>();
        for (GrantedAuthority a : authorities) authList.add(a.getAuthority());
        return String.join(",", authList);
    }
}
