package com.schoolmanagementsystem.SchoolManagementSystem.service;

import com.schoolmanagementsystem.SchoolManagementSystem.GlobalExceptionHandler.DuplicateResourceException;
import com.schoolmanagementsystem.SchoolManagementSystem.GlobalExceptionHandler.ResourceNotFoundException;
import com.schoolmanagementsystem.SchoolManagementSystem.dtoMappers.SubjectAllocationMapper;
import com.schoolmanagementsystem.SchoolManagementSystem.dtos.SubjectAllocationDTO;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.Section;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.Subject;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.SubjectAllocation;
import com.schoolmanagementsystem.SchoolManagementSystem.repository.SectionSubjectRepository;
import com.schoolmanagementsystem.SchoolManagementSystem.repository.SubjectAllocationRepository;
import com.schoolmanagementsystem.SchoolManagementSystem.repository.SubjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SubjectAllocationService {


    private final SubjectAllocationRepository allocationRepository;
    private final SectionService sectionService;
    private final SubjectRepository subjectRepository;
    private final SubjectAllocationMapper mapper;
    private final SectionSubjectRepository sectionSubjectRepository;

    public SubjectAllocationService(SubjectAllocationRepository allocationRepository,
                                    SectionService sectionService,
                                    SubjectRepository subjectRepository, SubjectAllocationMapper mapper, SectionSubjectRepository sectionSubjectRepository) {
        this.allocationRepository = allocationRepository;
        this.sectionService = sectionService;
        this.subjectRepository = subjectRepository;
        this.mapper = mapper;
        this.sectionSubjectRepository = sectionSubjectRepository;
    }

    public SubjectAllocation allocateSubject(Long sectionId, Long subjectId, SubjectAllocation allocation) {
        Section section = sectionService.getSectionById(sectionId);
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject", "id", subjectId));

        validateAcademicYear(allocation.getAcademicYear());

        if (!sectionSubjectRepository.existsBySectionIdAndSubjectIdAndAcademicYear(
                sectionId, subjectId, allocation.getAcademicYear())) {
            throw new ResourceNotFoundException("Subject in curriculum", "sectionId-subjectId-academicYear",
                    sectionId + "-" + subjectId + "-" + allocation.getAcademicYear());
        }

        // Check if subject already allocated to this section in same academic year
        if (allocationRepository.existsBySectionIdAndSubjectIdAndAcademicYear(sectionId, subjectId, allocation.getAcademicYear())) {
            throw new DuplicateResourceException("SubjectAllocation", "academicYear", allocation.getAcademicYear());
        }

        allocation.setSection(section);
        allocation.setSubject(subject);
        return allocationRepository.save(allocation);
    }

    public List<SubjectAllocationDTO> getSubjectsBySection(Long sectionId) {
        return allocationRepository.findBySectionId(sectionId).stream()
                .map(mapper::toDTO)
                .toList();
    }

    public List<SubjectAllocationDTO> getTeacherSubjects(Long teacherId) {
        return allocationRepository.findByTeacher_Id(teacherId).stream()
                .map(mapper::toDTO)
                .toList();
    }

    public void removeSubjectAllocation(Long allocationId) {
        SubjectAllocation allocation = allocationRepository.findById(allocationId)
                .orElseThrow(() -> new ResourceNotFoundException("SubjectAllocation", "id", allocationId));
        allocationRepository.delete(allocation);
    }

    public void validateAcademicYear(String academicYear) {
        if (!academicYear.matches("\\d{4}-\\d{4}")) {
            throw new IllegalArgumentException("Invalid academic year format. Use YYYY-YYYY");
        }
        String[] years = academicYear.split("-");
        if (Integer.parseInt(years[1]) != Integer.parseInt(years[0]) + 1) {
            throw new IllegalArgumentException("Academic year must be consecutive years like 2024-2025");
        }
    }


}
