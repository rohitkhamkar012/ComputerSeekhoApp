package com.example.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.entities.Staff;
import jakarta.transaction.Transactional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {
    Optional<Staff> findByStaffUsername(String staffUsername);

    Optional<Staff> findByStaffUsernameIgnoreCase(String staffUsername);

    Optional<Staff> findByStaffEmail(String staffEmail);

    Optional<Staff> findByStaffEmailIgnoreCase(String staffEmail);

    @Modifying
    @Transactional
    @Query("UPDATE Staff s SET s.staffUsername = ?1, s.staffPassword = ?2 WHERE s.staffId = ?3")
    void updateStaff(String username, String password, int id);

    @Query("SELECT s.staffId FROM Staff s WHERE s.staffUsername = ?1")
    int getStaffIdByStaffUsername(String username);
}
