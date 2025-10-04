package com.schoolmanagementsystem.SchoolManagementSystem.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SectionSubjectsResponse {


    private Long id;

    private Long sectionId;

    private String sectionName;

    private String standardName;

    private Long subjectId;

    private String subjectName;

    private String subjectCode;

    private Integer periodsPerWeek;

    private String academicYear;

    private Boolean isCompulsory;

    private LocalDateTime createdAt;


}
