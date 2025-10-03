package com.schoolmanagementsystem.SchoolManagementSystem.controller;

import com.schoolmanagementsystem.SchoolManagementSystem.entity.SubjectAllocation;
import com.schoolmanagementsystem.SchoolManagementSystem.service.SubjectAllocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sections/{sectionId}/subject-allocations")
public class SubjectAllocationController {


    private final SubjectAllocationService allocationService;

    public SubjectAllocationController(SubjectAllocationService allocationService) {
        this.allocationService = allocationService;
    }

    @PostMapping("/subjects/{subjectId}")
    public ResponseEntity<SubjectAllocation> allocateSubject(
            @PathVariable Long sectionId,
            @PathVariable Long subjectId,
            @RequestBody SubjectAllocation allocation) {
        SubjectAllocation created = allocationService.allocateSubject(sectionId, subjectId, allocation);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<SubjectAllocation>> getSubjectAllocationsBySection(@PathVariable Long sectionId) {
        return ResponseEntity.ok(allocationService.getSubjectsBySection(sectionId));
    }

    @DeleteMapping("/{allocationId}")
    public ResponseEntity<Void> removeSubjectAllocation(@PathVariable Long allocationId) {
        allocationService.removeSubjectAllocation(allocationId);
        return ResponseEntity.ok().build();
    }


}
