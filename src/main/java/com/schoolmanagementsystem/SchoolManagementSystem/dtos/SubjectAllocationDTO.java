package com.schoolmanagementsystem.SchoolManagementSystem.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectAllocationDTO {


    private Long id;

    private String sectionName;

    private String standardName;

    private String subjectName;

    private String teacherName;

    private Integer periodsPerWeek;

    private String academicYear;


}