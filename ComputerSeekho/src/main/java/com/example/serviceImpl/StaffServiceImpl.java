package com.example.serviceImpl;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.entities.Staff;
import com.example.repositories.StaffRepository;
import com.example.services.StaffService;

@Service
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;

    public StaffServiceImpl(StaffRepository staffRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.staffRepository = staffRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Staff saveStaff(Staff staff) {
        if (!staff.getStaffRole().startsWith("ROLE_")) {
            staff.setStaffRole("ROLE_" + staff.getStaffRole());
        }
        staff.setStaffPassword(passwordEncoder.encode(staff.getStaffPassword()));
        return staffRepository.save(staff);
    }

    @Override
    public Staff findByStaffUsername(String staffUsername) {
        return staffRepository.findByStaffUsername(staffUsername).orElse(null);
    }

    @Override
    public boolean updateStaff(Staff staff) {
        if (staffRepository.existsById(staff.getStaffId())) {
            staff.setStaffPassword(passwordEncoder.encode(staff.getStaffPassword()));
            staffRepository.updateStaff(staff.getStaffUsername(), staff.getStaffPassword(), staff.getStaffId());
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteByStaffId(int staffId) {
        if (!staffRepository.existsById(staffId))
            return false;
        staffRepository.deleteById(staffId);
        return true;
    }

    @Override
    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    @Override
    public int getStaffIdByStaffUsername(String username) {
        return staffRepository.getStaffIdByStaffUsername(username);
    }

    @Override
    public Staff getStaffByEmail(String email) {
        Staff staff = staffRepository.findByStaffEmailIgnoreCase(email).orElse(null);
        if (staff == null) {
            // Fallback: Check if they used their email as their username (Case Insensitive)
            staff = staffRepository.findByStaffUsernameIgnoreCase(email).orElse(null);
        }
        return staff;
    }
}
