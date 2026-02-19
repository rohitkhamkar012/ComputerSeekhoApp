package com.example.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.entities.News;
import com.example.entities.Staff;
import com.example.repositories.NewsRepository;
import com.example.repositories.StaffRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class DataLoader implements CommandLineRunner {

    private final StaffRepository staffRepository;
    private final NewsRepository newsRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(StaffRepository staffRepository, NewsRepository newsRepository, PasswordEncoder passwordEncoder) {
        this.staffRepository = staffRepository;
        this.newsRepository = newsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (staffRepository.findByStaffUsername("admin").isEmpty()) {
            Staff admin = new Staff();
            admin.setStaffName("Admin User");
            admin.setStaffUsername("admin");
            admin.setStaffEmail("admin@example.com");
            admin.setStaffMobile("9999999999");
            admin.setStaffGender("Male");
            admin.setPhotoUrl("http://img.com/admin.jpg");
            admin.setStaffRole("ROLE_ADMIN");
            admin.setStaffPassword(passwordEncoder.encode("admin@123"));
            staffRepository.save(admin);
        }

        if (newsRepository.count() == 0) {
            News news = new News();
            news.setNewsTitle("Welcome to Computer Seekho");
            news.setNewsDescription("New batches starting from next Monday. Enroll now!");
            news.setNewsUrl("http://example.com");
            newsRepository.save(news);
        }
    }
}
