package com.example.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.configuration.SecurityConstant;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTTokenValidatorFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Get JWT from Authorization header
        String jwt = request.getHeader(SecurityConstant.JWT_HEADER);
        if (jwt != null && jwt.startsWith("Bearer ")) {
            try {
                jwt = jwt.substring(7); // remove "Bearer "
                SecretKey key = Keys.hmacShaKeyFor(SecurityConstant.JWT_KEY.getBytes(StandardCharsets.UTF_8));
                Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();

                String username = claims.get("username", String.class);
                String authorities = claims.get("authorities", String.class);

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        username, null, AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception e) {
                // Token is invalid/expired.
                // Do NOT throw exception here, because that blocks access even to public
                // endpoints (like /login or /course/getAll).
                // Instead, just clear context (or do nothing) and let the chain continue.
                // SecurityConfig will reject if authentication was actually required.
                SecurityContextHolder.clearContext();
            }
        }

        // Continue chain regardless of whether token was present.
        // If missing, SecurityContext is empty. SecurityConfig will block if needed.
        chain.doFilter(request, response);
    }
}
