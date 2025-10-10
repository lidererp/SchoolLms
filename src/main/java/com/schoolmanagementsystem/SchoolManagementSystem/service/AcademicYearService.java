package com.schoolmanagementsystem.SchoolManagementSystem.service;

import com.schoolmanagementsystem.SchoolManagementSystem.entity.AcademicYear;
import com.schoolmanagementsystem.SchoolManagementSystem.repository.AcademicYearRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AcademicYearService {


    private final AcademicYearRepository academicYearRepository;

    public AcademicYearService(AcademicYearRepository academicYearRepository) {
        this.academicYearRepository = academicYearRepository;
    }

    @Transactional
    public AcademicYear createAcademicYear(AcademicYear academicYear) {
        return academicYearRepository.save(academicYear);
    }

    public List<AcademicYear> getAllAcademicYears() {
        return academicYearRepository.findAll();
    }

    public AcademicYear getAcademicYear(Long id) {
        return academicYearRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Academic year not found"));
    }


}
