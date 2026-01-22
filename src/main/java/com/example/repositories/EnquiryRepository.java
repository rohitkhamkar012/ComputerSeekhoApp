package com.example.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.entities.Enquiry;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface EnquiryRepository extends JpaRepository<Enquiry, Integer> {

    // 1️⃣ Get enquiries by staff (only active)
    @Query(value = """
        SELECT * 
        FROM enquiry 
        WHERE staff_id = (
            SELECT staff_id FROM staff WHERE staff_username = :staffUsername
        )
        AND enquiry_is_active = true
        ORDER BY follow_up_date
        """, nativeQuery = true)
    List<Enquiry> getByStaffList(@Param("staffUsername") String staffUsername);

    // ✅ FIXED METHOD NAME
    List<Enquiry> findByEnquiryIsActiveTrue();

    // 3️⃣ Update enquiry message & increment counter
    @Modifying
    @Query("""
        UPDATE Enquiry e
        SET e.enquirerQuery = :enquirerQuery,
            e.enquiryCounter = e.enquiryCounter + 1
        WHERE e.enquiryId = :enquiryId
    """)
    int updateMessage(@Param("enquiryId") int enquiryId,
                      @Param("enquirerQuery") String enquirerQuery);
}
