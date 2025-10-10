package com.schoolmanagementsystem.SchoolManagementSystem.repository;

import com.schoolmanagementsystem.SchoolManagementSystem.entity.AcademicYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademicYearRepository extends JpaRepository<AcademicYear, Long> {

}