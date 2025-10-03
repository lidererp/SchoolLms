package com.schoolmanagementsystem.SchoolManagementSystem.service;

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

    public TeacherAssignmentService(TeacherAssignmentRepository assignmentRepository, SectionService sectionService) {
        this.assignmentRepository = assignmentRepository;
        this.sectionService = sectionService;
    }

    public TeacherAssignment assignClassTeacher(Long sectionId, TeacherAssignment assignment) {
        Section section = sectionService.getSectionById(sectionId);

        // Check if class teacher already assigned for current year
        if (assignmentRepository.existsBySectionIdAndAcademicYearAndIsCurrent(sectionId, assignment.getAcademicYear(), true)) {
            throw new RuntimeException("Class teacher already assigned for this section in academic year: " + assignment.getAcademicYear());
        }

        assignment.setSection(section);
        assignment.setIsCurrent(true);
        return assignmentRepository.save(assignment);
    }

    public List<TeacherAssignment> getClassTeachersBySection(Long sectionId) {
        return assignmentRepository.findBySectionId(sectionId);
    }

    public List<TeacherAssignment> getClassTeachersByAcademicYear(String academicYear) {
        return assignmentRepository.findByAcademicYear(academicYear);
    }

    public void removeClassTeacher(Long assignmentId) {
        TeacherAssignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Teacher assignment not found"));
        assignment.setIsCurrent(false);
        assignmentRepository.save(assignment);
    }


}
