package com.schoolmanagementsystem.SchoolManagementSystem.dtos;



import lombok.Data;

import java.util.List;

@Data
public class AssignSectionRequestDTO {
    private List<Long> studentIds;
    private Long sectionId;
}