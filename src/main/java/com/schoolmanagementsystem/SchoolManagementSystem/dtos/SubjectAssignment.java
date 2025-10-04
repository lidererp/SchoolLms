package com.schoolmanagementsystem.SchoolManagementSystem.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubjectAssignment {


    @NotNull(message = "Subject ID is required")
    private Long subjectId;


}