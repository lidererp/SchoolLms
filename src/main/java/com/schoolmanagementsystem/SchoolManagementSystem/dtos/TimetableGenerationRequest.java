package com.schoolmanagementsystem.SchoolManagementSystem.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimetableGenerationRequest {

    private Long configId;

    private Map<String, List<Long>> subjectAllocationMapping;


}
