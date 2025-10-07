package com.schoolmanagementsystem.SchoolManagementSystem.repository;

import com.schoolmanagementsystem.SchoolManagementSystem.entity.Syllabus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SyllabusRepository extends JpaRepository<Syllabus, Long> {

}
