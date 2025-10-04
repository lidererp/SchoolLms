package com.schoolmanagementsystem.SchoolManagementSystem.controller;

import com.schoolmanagementsystem.SchoolManagementSystem.dtos.SectionSubjectsRequest;
import com.schoolmanagementsystem.SchoolManagementSystem.dtos.SectionSubjectsResponse;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.SubjectAllocation;
import com.schoolmanagementsystem.SchoolManagementSystem.service.SectionSubjectService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sections")
public class SectionSubjectController {


    private final SectionSubjectService sectionSubjectService;

    public SectionSubjectController(SectionSubjectService sectionSubjectService) {
        this.sectionSubjectService = sectionSubjectService;
    }

    @PostMapping("/subjects")
    public ResponseEntity<List<SectionSubjectsResponse>> assignSubjectsToSection(
            @Valid @RequestBody SectionSubjectsRequest request) {

        List<SectionSubjectsResponse> responses = sectionSubjectService.assignSubjectsToSection(request);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{sectionId}/subjects")
    public ResponseEntity<List<SectionSubjectsResponse>> getSectionSubjects(@PathVariable Long sectionId) {
        List<SectionSubjectsResponse> responses = sectionSubjectService.getSectionSubjects(sectionId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{sectionId}/subjects/{academicYear}")
    public ResponseEntity<List<SectionSubjectsResponse>> getSectionSubjectsByAcademicYear(
            @PathVariable Long sectionId,
            @PathVariable String academicYear) {

        List<SectionSubjectsResponse> responses = sectionSubjectService.getSectionSubjectsByAcademicYear(sectionId, academicYear);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/subjects/{sectionSubjectId}")
    public ResponseEntity<Void> removeSubjectFromSection(@PathVariable Long sectionSubjectId) {
        sectionSubjectService.removeSubjectFromSection(sectionSubjectId);
        return ResponseEntity.ok().build();
    }


}
