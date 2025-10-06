package com.schoolmanagementsystem.SchoolManagementSystem.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SyllabusDTO {


    @NotNull(message = "Unit number is required")
    private Integer unitNumber;

    @NotBlank(message = "Unit title is required")
    private String unitTitle;

    @NotBlank(message = "Topics cannot be empty")
    private String topics;

    private Integer estimatedHours;


}

