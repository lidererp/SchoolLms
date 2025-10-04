package com.schoolmanagementsystem.SchoolManagementSystem.repository;

import com.schoolmanagementsystem.SchoolManagementSystem.entity.TeacherAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherAssignmentRepository extends JpaRepository<TeacherAssignment, Long> {


    List<TeacherAssignment> findBySectionId(Long sectionId);

    List<TeacherAssignment> findByTeacher_Id(Long teacherId);

    boolean existsBySectionIdAndAcademicYearAndIsCurrent(Long sectionId, String academicYear, Boolean isCurrent);



}