package com.schoolmanagementsystem.SchoolManagementSystem.service;

import com.schoolmanagementsystem.SchoolManagementSystem.GlobalExceptionHandler.DuplicateResourceException;
import com.schoolmanagementsystem.SchoolManagementSystem.GlobalExceptionHandler.ResourceNotFoundException;
import com.schoolmanagementsystem.SchoolManagementSystem.dtoMappers.TeacherAssignmentMapper;
import com.schoolmanagementsystem.SchoolManagementSystem.dtos.SubjectAllocationDTO;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.Section;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.TeacherAssignment;
import com.schoolmanagementsystem.SchoolManagementSystem.repository.TeacherAssignmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TeacherAssignmentService {


    private final TeacherAssignmentRepository assignmentRepository;
    private final SectionService sectionService;
    private final TeacherAssignmentMapper mapper;
    private final SubjectAllocationService subjectAllocationService;


    public TeacherAssignmentService(TeacherAssignmentRepository assignmentRepository, SectionService sectionService, TeacherAssignmentMapper mapper, SubjectAllocationService subjectAllocationService) {
        this.assignmentRepository = assignmentRepository;
        this.sectionService = sectionService;
        this.mapper = mapper;
        this.subjectAllocationService = subjectAllocationService;
    }

    public TeacherAssignment assignClassTeacher(Long sectionId, TeacherAssignment assignment) {
        Section section = sectionService.getSectionById(sectionId);

        subjectAllocationService.validateAcademicYear(assignment.getAcademicYear());

        // Check if class teacher already assigned for current year
        if (assignmentRepository.existsBySectionIdAndAcademicYearAndIsCurrent(sectionId, assignment.getAcademicYear(), true)) {
            throw new DuplicateResourceException(
                    "TeacherAssignment",
                    "sectionId-academicYear",
                    sectionId + "-" + assignment.getAcademicYear()
            );
        }

        assignment.setSection(section);
        assignment.setIsCurrent(true);
        return assignmentRepository.save(assignment);
    }

    public List<SubjectAllocationDTO> getClassTeachersBySection(Long sectionId) {
        return assignmentRepository.findBySectionId(sectionId).stream()
                .map(mapper::toDTO)
                .toList();
    }

    public List<SubjectAllocationDTO> getTeacherAssignments(Long teacherId) {
        return assignmentRepository.findByTeacher_Id(teacherId).stream()
                .map(mapper::toDTO)
                .toList();
    }

    public void removeClassTeacher(Long assignmentId) {
        TeacherAssignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("TeacherAssignment", "id", assignmentId));
        assignment.setIsCurrent(false);
        assignmentRepository.save(assignment);
    }


}
