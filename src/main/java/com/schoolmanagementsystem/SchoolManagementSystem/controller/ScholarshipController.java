package com.schoolmanagementsystem.SchoolManagementSystem.controller;

import com.schoolmanagementsystem.SchoolManagementSystem.entity.Scholarship;
import com.schoolmanagementsystem.SchoolManagementSystem.service.ScholarshipService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/scholarships")
public class ScholarshipController {


    private final ScholarshipService service;

    public ScholarshipController(ScholarshipService service) {
        this.service = service;
    }


    @GetMapping
    public ResponseEntity<List<Scholarship>> getAllScholarships(
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size) {

        List<Scholarship> scholarships = (cursor == null)
                ? service.getFirstPage(size)
                : service.getScholarshipsAfterCursor(cursor, size);

        return ResponseEntity.ok(scholarships);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Scholarship> getById(@PathVariable Long id) {
        Scholarship scholarship = service.getById(id);
        return (scholarship != null) ? ResponseEntity.ok(scholarship) : ResponseEntity.notFound().build();
    }


    @PostMapping
    public ResponseEntity<Scholarship> create(@RequestBody Scholarship scholarship) {
        return ResponseEntity.ok(service.save(scholarship));
    }


    @PutMapping("/{id}")
    public ResponseEntity<Scholarship> update(@PathVariable Long id, @RequestBody Scholarship s) {
        Scholarship updated = service.updateScholarship(id, s);
        return (updated != null) ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }


}

