package com.schoolmanagementsystem.SchoolManagementSystem.repository;

import com.schoolmanagementsystem.SchoolManagementSystem.entity.Student;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {


    @Query("SELECT s FROM Student s WHERE s.id < :cursor ORDER BY s.id DESC")
    List<Student> findWithCursorDesc(@Param("cursor") Long cursor, Pageable pageable);

    // You can add custom queries here if needed
    Student findByAdmissionNumber(String admissionNumber);

    boolean existsByStudentEmail(String email);

    List<Student> findByStandardId(Long standardId);

    boolean existsByAdmissionNumber(String admissionNumber);

    @Query("""
            SELECT s 
            FROM Student s 
            JOIN s.academicRecords ar 
            WHERE s.standard.id = :standardId 
            AND ar.academicYear = :academicYear
            """)
    List<Student> findByStandardIdAndAcademicYear(
            @Param("standardId") Long standardId,
            @Param("academicYear") String academicYear
    );

    @Query("SELECT s FROM Student s JOIN s.academicRecords ar " +
            "WHERE s.standard.id = :standardId AND ar.academicYear = :academicYear " +
            "AND s.section.id = :sectionId")
    List<Student> findByStandardIdAndAcademicYearAndSectionId(
            @Param("standardId") Long standardId,
            @Param("academicYear") String academicYear,
            @Param("sectionId") Long sectionId
    );


}
