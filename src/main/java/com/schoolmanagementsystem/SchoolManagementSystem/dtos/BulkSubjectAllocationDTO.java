package com.schoolmanagementsystem.SchoolManagementSystem.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class BulkSubjectAllocationDTO {


    @NotNull(message = "Academic year is required")
    private String academicYear;

    @NotNull(message = "Allocations list cannot be null")
    private List<SubjectAllocationItemDTO> allocations;

    @Data
    public static class SubjectAllocationItemDTO {
        @NotNull(message = "Subject ID is required")
        private Long subjectId;

        @NotNull(message = "Teacher ID is required")
        private Long teacherId;

        @NotNull(message = "Periods per week is required")
        private Integer periodsPerWeek;
    }


}
