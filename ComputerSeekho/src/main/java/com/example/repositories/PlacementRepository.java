package com.example.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.DTO.PlacedStudentDTO;
import com.example.entities.Placement;

@Repository
public interface PlacementRepository extends JpaRepository<Placement, Integer> {

    @Query("""
            SELECT new com.example.DTO.PlacedStudentDTO(
                b.batchId,
                b.batchName,
                s.studentName,
                s.photoUrl,
                r.recruiterName
            )
            FROM Placement p
            JOIN p.batch b
            JOIN p.studentID s
            JOIN p.recruiterID r
            ORDER BY b.batchId
            """)
    List<PlacedStudentDTO> fetchPlacedStudents();

    @Query("""
            SELECT new com.example.DTO.PlacedStudentDTO(
                b.batchId,
                b.batchName,
                s.studentName,
                s.photoUrl,
                r.recruiterName
            )
            FROM Placement p
            JOIN p.batch b
            JOIN p.studentID s
            JOIN p.recruiterID r
            WHERE b.batchId = :batchId
            ORDER BY b.batchId
            """)
    List<PlacedStudentDTO> findByBatchId(@Param("batchId") Integer batchId);

    @Query("""
            SELECT new com.example.DTO.PlacedStudentDTO(
                p.batch.batchId,
                p.batch.batchName,
                p.studentID.studentName,
                p.studentID.photoUrl,
                p.recruiterID.recruiterName
            )
            FROM Placement p
            WHERE p.recruiterID.recruiterId = :recruiterId
            ORDER BY p.placementID
            """)
    List<PlacedStudentDTO> findByRecruiterId(@Param("recruiterId") Integer recruiterId);
}
