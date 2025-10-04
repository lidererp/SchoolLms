package com.schoolmanagementsystem.SchoolManagementSystem.repository;

import com.schoolmanagementsystem.SchoolManagementSystem.entity.SectionSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionSubjectRepository extends JpaRepository<SectionSubject, Long> {


    List<SectionSubject> findBySectionId(Long sectionId);

    List<SectionSubject> findBySectionIdAndAcademicYear(Long sectionId, String academicYear);

    boolean existsBySectionIdAndSubjectIdAndAcademicYear(Long sectionId, Long subjectId, String academicYear);

    List<SectionSubject> findBySubjectId(Long subjectId);


}