package com.schoolmanagementsystem.SchoolManagementSystem.dtos;

import lombok.Data;

import java.util.List;

@Data
public class CurriculumSubjectResponseDTO {


    private Long id;

    private String subjectName;

    private List<SyllabusResponseDTO> syllabusList;


}

