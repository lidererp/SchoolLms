package com.schoolmanagementsystem.SchoolManagementSystem.dtoMappers;

import com.schoolmanagementsystem.SchoolManagementSystem.dtos.SubjectAllocationDTO;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.TeacherAssignment;
import org.springframework.stereotype.Component;

@Component
public class TeacherAssignmentMapper {


    public SubjectAllocationDTO toDTO(TeacherAssignment assignment) {
        if (assignment == null) return null;

        SubjectAllocationDTO dto = new SubjectAllocationDTO();
        dto.setId(assignment.getId());
        dto.setSectionName(assignment.getSection().getSectionName());
        dto.setStandardName(assignment.getSection().getStandard().getClassName());
        dto.setTeacherName(assignment.getTeacher().getEmployeeName());
        dto.setAcademicYear(assignment.getAcademicYear());
//        dto.setAssignedDate(assignment.getAssignedDate());
//        dto.setEndDate(assignment.getEndDate());
//        dto.setIsCurrent(assignment.getIsCurrent());
        return dto;
    }


}
