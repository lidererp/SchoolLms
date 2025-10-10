package com.schoolmanagementsystem.SchoolManagementSystem.controller;

import com.schoolmanagementsystem.SchoolManagementSystem.entity.AcademicYear;
import com.schoolmanagementsystem.SchoolManagementSystem.service.AcademicYearService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/academic-years")
public class AcademicYearController {


    private final AcademicYearService academicYearService;

    public AcademicYearController(AcademicYearService academicYearService) {
        this.academicYearService = academicYearService;
    }

    @PostMapping
    public ResponseEntity<AcademicYear> create(@RequestBody AcademicYear academicYear) {
        return ResponseEntity.ok(academicYearService.createAcademicYear(academicYear));
    }

    @GetMapping
    public ResponseEntity<List<AcademicYear>> getAll() {
        return ResponseEntity.ok(academicYearService.getAllAcademicYears());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AcademicYear> get(@PathVariable Long id) {
        return ResponseEntity.ok(academicYearService.getAcademicYear(id));
    }


}
