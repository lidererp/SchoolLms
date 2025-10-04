package com.schoolmanagementsystem.SchoolManagementSystem.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class SectionSubjectsRequest {


    @NotNull(message = "Section ID is required")
    private Long sectionId;

    @NotBlank(message = "Academic year is required")
    private String academicYear;

    @Valid
    @NotNull(message = "Subjects are required")
    @Size(min = 1, message = "At least one subject is required")
    private List<SubjectAssignment> subjects;


}