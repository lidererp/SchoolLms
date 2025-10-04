package com.schoolmanagementsystem.SchoolManagementSystem.dtoMappers;

import com.schoolmanagementsystem.SchoolManagementSystem.dtos.SubjectAllocationDTO;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.SubjectAllocation;
import org.springframework.stereotype.Component;

@Component
public class SubjectAllocationMapper {


    public SubjectAllocationDTO toDTO(SubjectAllocation allocation) {
        if (allocation == null) return null;

        SubjectAllocationDTO dto = new SubjectAllocationDTO();
        dto.setId(allocation.getId());
        dto.setSectionName(allocation.getSection().getSectionName());
        dto.setStandardName(allocation.getSection().getStandard().getClassName());
        dto.setSubjectName(allocation.getSubject().getSubjectName());
        dto.setTeacherName(allocation.getTeacher().getEmployeeName());
        dto.setPeriodsPerWeek(allocation.getPeriodsPerWeek());
        dto.setAcademicYear(allocation.getAcademicYear());
        return dto;
    }


}