package com.schoolmanagementsystem.SchoolManagementSystem.dtos;

import com.schoolmanagementsystem.SchoolManagementSystem.enums.CurriculumScope;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurriculumResponseDTO {


    private Long id;

    private CurriculumScope scopeType;

    private Long scopeId;

    private String academicYear;

    private List<CurriculumSubjectResponseDTO> curriculumSubjects;


}
