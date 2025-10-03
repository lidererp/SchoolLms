package com.schoolmanagementsystem.SchoolManagementSystem.repository;


import com.schoolmanagementsystem.SchoolManagementSystem.entity.SubjectAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectAllocationRepository extends JpaRepository<SubjectAllocation, Long> {


    List<SubjectAllocation> findBySectionId(Long sectionId);

    List<SubjectAllocation> findByTeacherName(String teacherName);

    List<SubjectAllocation> findByAcademicYear(String academicYear);

    List<SubjectAllocation> findBySubjectId(Long subjectId);

    Optional<SubjectAllocation> findBySectionIdAndSubjectIdAndAcademicYear(Long sectionId, Long subjectId, String academicYear);

    boolean existsBySectionIdAndSubjectIdAndAcademicYear(Long sectionId, Long subjectId, String academicYear);


}