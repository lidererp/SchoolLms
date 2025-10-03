package com.schoolmanagementsystem.SchoolManagementSystem.service;

import com.schoolmanagementsystem.SchoolManagementSystem.dtoMappers.SubjectAllocationMapper;
import com.schoolmanagementsystem.SchoolManagementSystem.dtos.SubjectAllocationDTO;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.Section;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.Subject;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.SubjectAllocation;
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

    public SubjectAllocationService(SubjectAllocationRepository allocationRepository,
                                    SectionService sectionService,
                                    SubjectRepository subjectRepository, SubjectAllocationMapper mapper) {
        this.allocationRepository = allocationRepository;
        this.sectionService = sectionService;
        this.subjectRepository = subjectRepository;
        this.mapper = mapper;
    }

    public SubjectAllocation allocateSubject(Long sectionId, Long subjectId, SubjectAllocation allocation) {
        Section section = sectionService.getSectionById(sectionId);
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + subjectId));

        validateAcademicYear(allocation.getAcademicYear());

        // Check if subject already allocated to this section in same academic year
        if (allocationRepository.existsBySectionIdAndSubjectIdAndAcademicYear(sectionId, subjectId, allocation.getAcademicYear())) {
            throw new RuntimeException("Subject already allocated to this section in academic year: " + allocation.getAcademicYear());
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
                .orElseThrow(() -> new RuntimeException("Subject allocation not found"));
        allocationRepository.delete(allocation);
    }

    public void validateAcademicYear(String academicYear) {
        if (!academicYear.matches("\\d{4}-\\d{4}")) {
            throw new RuntimeException("Invalid academic year format. Use YYYY-YYYY");
        }
        String[] years = academicYear.split("-");
        if (Integer.parseInt(years[1]) != Integer.parseInt(years[0]) + 1) {
            throw new RuntimeException("Academic year must be consecutive years like 2024-2025");
        }
    }


}
