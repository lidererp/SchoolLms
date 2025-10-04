package com.schoolmanagementsystem.SchoolManagementSystem.repository;


import com.schoolmanagementsystem.SchoolManagementSystem.entity.SubjectAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectAllocationRepository extends JpaRepository<SubjectAllocation, Long> {


    List<SubjectAllocation> findBySectionId(Long sectionId);

    List<SubjectAllocation> findByTeacher_Id(Long teacherId);

    boolean existsBySectionIdAndSubjectIdAndAcademicYear(Long sectionId, Long subjectId, String academicYear);


}