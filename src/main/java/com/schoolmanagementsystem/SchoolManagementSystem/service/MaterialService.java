package com.schoolmanagementsystem.SchoolManagementSystem.service;

import com.schoolmanagementsystem.SchoolManagementSystem.GlobalExceptionHandler.ResourceNotFoundException;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.AcademicYear;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.Material;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.Section;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.Standard;
import com.schoolmanagementsystem.SchoolManagementSystem.repository.AcademicYearRepository;
import com.schoolmanagementsystem.SchoolManagementSystem.repository.MaterialRepository;
import com.schoolmanagementsystem.SchoolManagementSystem.repository.SectionRepository;
import com.schoolmanagementsystem.SchoolManagementSystem.repository.StandardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MaterialService {


    private final MaterialRepository materialRepository;
    private final StandardRepository standardRepository;
    private final SectionRepository sectionRepository;
    private final AcademicYearRepository academicYearRepository;

    public MaterialService(MaterialRepository materialRepository,
                           StandardRepository standardRepository,
                           SectionRepository sectionRepository,
                           AcademicYearRepository academicYearRepository) {
        this.materialRepository = materialRepository;
        this.standardRepository = standardRepository;
        this.sectionRepository = sectionRepository;
        this.academicYearRepository = academicYearRepository;
    }

    @Transactional
    public Material uploadMaterialByUrl(String fileUrl,
                                        Long standardId,
                                        Long sectionId,
                                        Long academicYearId,
                                        String description) {

        validateStandardOrSection(standardId, sectionId);

        AcademicYear academicYear = academicYearRepository.findById(academicYearId)
                .orElseThrow(() -> new ResourceNotFoundException("AcademicYear", "id", academicYearId));

        Standard standard = null;
        Section section = null;

        if (standardId != null) {
            standard = standardRepository.findById(standardId)
                    .orElseThrow(() -> new ResourceNotFoundException("Standard", "id", standardId));
        }

        if (sectionId != null) {
            section = sectionRepository.findById(sectionId)
                    .orElseThrow(() -> new ResourceNotFoundException("Section", "id", sectionId));
        }

        Material material = new Material();
        material.setFileName(fileUrl.substring(fileUrl.lastIndexOf("/") + 1));
        material.setFileType(null); // optional
        material.setFileUrl(fileUrl);
        material.setDescription(description);
        material.setUploadedAt(LocalDateTime.now());
        material.setStandard(standard);
        material.setSection(section);
        material.setAcademicYear(academicYear);

        return materialRepository.save(material);
    }

    @Transactional
    public Material updateMaterialByUrl(Long id,
                                        String fileUrl,
                                        Long standardId,
                                        Long sectionId,
                                        Long academicYearId,
                                        String description) {

        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Material", "id", id));

        if (standardId != null) {
            Standard standard = standardRepository.findById(standardId)
                    .orElseThrow(() -> new ResourceNotFoundException("Standard", "id", standardId));
            material.setStandard(standard);
        }

        if (sectionId != null) {
            Section section = sectionRepository.findById(sectionId)
                    .orElseThrow(() -> new ResourceNotFoundException("Section", "id", sectionId));
            material.setSection(section);
        }

        if (academicYearId != null) {
            AcademicYear academicYear = academicYearRepository.findById(academicYearId)
                    .orElseThrow(() -> new ResourceNotFoundException("AcademicYear", "id", academicYearId));
            material.setAcademicYear(academicYear);
        }

        if (fileUrl != null) {
            material.setFileUrl(fileUrl);
            material.setFileName(fileUrl.substring(fileUrl.lastIndexOf("/") + 1));
        }

        if (description != null) {
            material.setDescription(description);
        }

        return materialRepository.save(material);
    }

    @Transactional
    public void deleteMaterial(Long id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Material", "id", id));
        materialRepository.delete(material);
    }


    public List<Material> getMaterials(Long standardId, Long sectionId, Long academicYearId) {
        return materialRepository.findByStandardIdAndSectionIdAndAcademicYearId(
                standardId, sectionId, academicYearId);
    }


    private void validateStandardOrSection(Long standardId, Long sectionId) {
        if (standardId == null && sectionId == null) {
            throw new IllegalArgumentException("At least Standard or Section must be chosen");
        }
    }

    public List<Material> getAllMaterials() {
        return materialRepository.findAll();
    }


}

