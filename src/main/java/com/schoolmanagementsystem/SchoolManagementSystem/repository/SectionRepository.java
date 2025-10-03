package com.schoolmanagementsystem.SchoolManagementSystem.repository;


import com.schoolmanagementsystem.SchoolManagementSystem.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {


    List<Section> findByStandardId(Long standardId);

    Optional<Section> findByStandardIdAndSectionName(Long standardId, String sectionName);

    boolean existsByStandardIdAndSectionName(Long standardId, String sectionName);


}