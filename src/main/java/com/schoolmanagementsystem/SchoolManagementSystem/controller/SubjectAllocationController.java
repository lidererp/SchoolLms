package com.schoolmanagementsystem.SchoolManagementSystem.controller;

import com.schoolmanagementsystem.SchoolManagementSystem.dtos.BulkSubjectAllocationDTO;
import com.schoolmanagementsystem.SchoolManagementSystem.dtos.SubjectAllocationDTO;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.SubjectAllocation;
import com.schoolmanagementsystem.SchoolManagementSystem.service.SubjectAllocationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sections/{sectionId}")
public class SubjectAllocationController {


    private final SubjectAllocationService allocationService;

    public SubjectAllocationController(SubjectAllocationService allocationService) {
        this.allocationService = allocationService;
    }

    @PostMapping("/{subjectId}")
    public ResponseEntity<SubjectAllocation> allocateSubject(
            @Valid @PathVariable Long sectionId,
            @PathVariable Long subjectId,
            @RequestBody SubjectAllocation allocation) {
        SubjectAllocation created = allocationService.allocateSubject(sectionId, subjectId, allocation);
        return ResponseEntity.ok(created);
    }

    @PostMapping
    public ResponseEntity<List<SubjectAllocation>> allocateMultipleSubjects(
            @PathVariable Long sectionId,
            @Valid @RequestBody BulkSubjectAllocationDTO dto) {
        List<SubjectAllocation> created = allocationService.allocateMultipleSubjects(sectionId, dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<SubjectAllocationDTO>> getSubjectAllocationsBySection(@PathVariable Long sectionId) {
        return ResponseEntity.ok(allocationService.getSubjectsBySection(sectionId));
    }

    @DeleteMapping("/{allocationId}")
    public ResponseEntity<Void> removeSubjectAllocation(@PathVariable Long allocationId) {
        allocationService.removeSubjectAllocation(allocationId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{teacherId}")
    public ResponseEntity<List<SubjectAllocationDTO>> getTeacherSubjects(@PathVariable Long teacherId) {
        return ResponseEntity.ok(allocationService.getTeacherSubjects(teacherId));
    }

}
