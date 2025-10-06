package com.schoolmanagementsystem.SchoolManagementSystem.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CurriculumSubjectDTO {

    @NotNull(message = "Subject ID is required")
    private Long subjectId;

    @NotEmpty(message = "Syllabus list cannot be empty")
    private List<SyllabusDTO> syllabusList;


}
