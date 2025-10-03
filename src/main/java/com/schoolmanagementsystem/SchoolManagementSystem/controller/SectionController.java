package com.schoolmanagementsystem.SchoolManagementSystem.controller;

import com.schoolmanagementsystem.SchoolManagementSystem.dtos.ClassDTO;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.Section;
import com.schoolmanagementsystem.SchoolManagementSystem.service.SectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("standards/{standardId}/sections")
public class SectionController {


    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<Section> createSection(@PathVariable Long standardId, @RequestBody Section section) {
        Section created = sectionService.createSection(standardId, section);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<Section>> getSectionsByStandard(@PathVariable Long standardId) {
        return ResponseEntity.ok(sectionService.getSectionsByStandard(standardId));
    }

    @DeleteMapping("/{sectionId}")
    public ResponseEntity<Void> deleteSection(@PathVariable Long sectionId) {
        sectionService.deleteSection(sectionId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{sectionId}")
    public ResponseEntity<Section> updateSection(
            @PathVariable Long standardId,
            @PathVariable Long sectionId,
            @RequestBody Section sectionDetails) {

        Section updated = sectionService.updateSection(standardId, sectionId, sectionDetails);
        return ResponseEntity.ok(updated);
    }


}