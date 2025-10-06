package com.schoolmanagementsystem.SchoolManagementSystem.dtos;

import lombok.Data;

@Data
public class SyllabusResponseDTO {


    private Integer unitNumber;

    private String unitTitle;

    private String topics;

    private Integer estimatedHours;


}
