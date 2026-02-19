package com.example.configuration;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.example.filter.JWTTokenValidatorFilter;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable());
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/i18n/**", "/i18n/greet").permitAll()
                .requestMatchers("/auth/signIn", "/auth/google-login").permitAll()
                .requestMatchers("/staff/**").permitAll()
                .requestMatchers("/News/**").permitAll()
                .requestMatchers("/course/**").permitAll()
                .requestMatchers("/recruiter/**").permitAll()
                .requestMatchers("/placement/**").permitAll()
                .requestMatchers("/enquiries/**").permitAll()
                .requestMatchers("/student/**").permitAll()
                .requestMatchers("/getInTouch/**").permitAll()
                .requestMatchers("/batch/**").permitAll()
                .requestMatchers("/api/excel/download").permitAll()
                .requestMatchers("/api/excel/upload").permitAll()

                .requestMatchers("/image/**").permitAll()
                .requestMatchers("/payment/**").permitAll()
                .requestMatchers("/error").permitAll()
                .requestMatchers("/auth/google-login").permitAll()
                .anyRequest().authenticated());

        // JWT Filter
        http.addFilterBefore(new JWTTokenValidatorFilter(), UsernamePasswordAuthenticationFilter.class);

        // CORS Configuration
        http.cors(cors -> cors.configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration cfg = new CorsConfiguration();
                cfg.setAllowedOriginPatterns(Collections.singletonList("*"));
                cfg.setAllowedMethods(Collections.singletonList("*"));
                cfg.setAllowedHeaders(Collections.singletonList("*"));
                cfg.setAllowCredentials(true);
                cfg.setExposedHeaders(Collections.singletonList("Authorization"));
                return cfg;
            }
        }));

        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http, UsernamePasswordAuthentication customAuthProvider)
            throws Exception {
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder.authenticationProvider(customAuthProvider);
        return authBuilder.build();
    }
}
