package com.schoolmanagementsystem.SchoolManagementSystem.repository;


import com.schoolmanagementsystem.SchoolManagementSystem.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {


    Optional<Subject> findBySubjectCode(String subjectCode);

    Optional<Subject> findBySubjectName(String subjectName);

    List<Subject> findBySubjectNameContainingIgnoreCase(String name);


}