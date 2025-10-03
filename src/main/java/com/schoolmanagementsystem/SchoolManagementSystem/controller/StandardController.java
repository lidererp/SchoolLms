package com.schoolmanagementsystem.SchoolManagementSystem.controller;

import com.schoolmanagementsystem.SchoolManagementSystem.dtos.ClassDTO;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.Section;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.Standard;
import com.schoolmanagementsystem.SchoolManagementSystem.service.SectionService;
import com.schoolmanagementsystem.SchoolManagementSystem.service.StandardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/standards")
public class StandardController {


    private final StandardService standardService;
    private final SectionService sectionService;

    public StandardController(StandardService standardService, SectionService sectionService) {
        this.standardService = standardService;
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<Standard> createStandard(@RequestBody Standard standard) {
        Standard created = standardService.createStandard(standard);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<Standard>> getAllStandards() {
        return ResponseEntity.ok(standardService.getAllStandards());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Standard> getStandardById(@PathVariable Long id) {
        return ResponseEntity.ok(standardService.getStandardById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Standard> updateStandard(@PathVariable Long id, @RequestBody Standard standard) {
        Standard updatedStandard = standardService.updateStandard(id, standard);
        return ResponseEntity.ok(updatedStandard);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStandard(@PathVariable Long id) {
        standardService.deleteStandard(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/sections/display")
    public List<ClassDTO> getSectionDisplayNames() {
        return sectionService.getSectionDisplayNames();
    }


}
