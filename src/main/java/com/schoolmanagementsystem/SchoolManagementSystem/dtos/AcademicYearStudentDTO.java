package com.schoolmanagementsystem.SchoolManagementSystem.dtos;

import com.schoolmanagementsystem.SchoolManagementSystem.enums.AcademicStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcademicYearStudentDTO {
    private Long studentId;
    private String studentName;
    private String standardName;
    private String admissionNumber;
    private AcademicStatus status;
    private String academicYear;
}
