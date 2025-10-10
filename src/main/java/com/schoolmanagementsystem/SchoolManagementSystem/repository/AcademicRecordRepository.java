package com.schoolmanagementsystem.SchoolManagementSystem.repository;



import com.schoolmanagementsystem.SchoolManagementSystem.entity.AcademicRecord;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AcademicRecordRepository extends JpaRepository<AcademicRecord,Long> {

    List<AcademicRecord> findByAcademicYear(String academicYear);

    List<AcademicRecord> findByStudentIdIn(List<Long> studentIds);

    @Query("SELECT ar.student FROM AcademicRecord ar WHERE ar.academicYear = :academicYear")
    List<Student> findStudentsByAcademicYear(String academicYear);

    Optional<AcademicRecord> findTopByStudentIdOrderByIdDesc(Long studentId);


}
