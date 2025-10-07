package com.schoolmanagementsystem.SchoolManagementSystem.dtos;

import com.schoolmanagementsystem.SchoolManagementSystem.enums.CurriculumScope;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CurriculumRequestDTO {

    @NotNull(message = "Scope type is required")
    private CurriculumScope scopeType;

    @NotNull(message = "Scope ID is required")
    private Long scopeId;

    @NotBlank(message = "Academic year is required")
    private String academicYear;

    @NotEmpty(message = "At least one subject must be provided")
    private List<CurriculumSubjectDTO> curriculumSubjects;


}