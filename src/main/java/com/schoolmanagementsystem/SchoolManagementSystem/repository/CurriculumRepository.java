package com.schoolmanagementsystem.SchoolManagementSystem.repository;

import com.schoolmanagementsystem.SchoolManagementSystem.entity.Curriculum;
import com.schoolmanagementsystem.SchoolManagementSystem.enums.CurriculumScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurriculumRepository extends JpaRepository<Curriculum, Long> {


    Optional<Curriculum> findByScopeTypeAndScopeIdAndAcademicYear(CurriculumScope type, Long id, String year);


}
