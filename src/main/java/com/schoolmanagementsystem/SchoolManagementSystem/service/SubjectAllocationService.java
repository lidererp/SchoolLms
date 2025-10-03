package com.schoolmanagementsystem.SchoolManagementSystem.service;

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

    public SubjectAllocationService(SubjectAllocationRepository allocationRepository,
                                    SectionService sectionService,
                                    SubjectRepository subjectRepository) {
        this.allocationRepository = allocationRepository;
        this.sectionService = sectionService;
        this.subjectRepository = subjectRepository;
    }

    public SubjectAllocation allocateSubject(Long sectionId, Long subjectId, SubjectAllocation allocation) {
        Section section = sectionService.getSectionById(sectionId);
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + subjectId));

        // Check if subject already allocated to this section in same academic year
        if (allocationRepository.existsBySectionIdAndSubjectIdAndAcademicYear(sectionId, subjectId, allocation.getAcademicYear())) {
            throw new RuntimeException("Subject already allocated to this section in academic year: " + allocation.getAcademicYear());
        }

        allocation.setSection(section);
        allocation.setSubject(subject);
        return allocationRepository.save(allocation);
    }

    public List<SubjectAllocation> getSubjectsBySection(Long sectionId) {
        return allocationRepository.findBySectionId(sectionId);
    }

    public List<SubjectAllocation> getTeacherSubjects(String teacherName) {
        return allocationRepository.findByTeacherName(teacherName);
    }

    public void removeSubjectAllocation(Long allocationId) {
        SubjectAllocation allocation = allocationRepository.findById(allocationId)
                .orElseThrow(() -> new RuntimeException("Subject allocation not found"));
        allocationRepository.delete(allocation);
    }


}
