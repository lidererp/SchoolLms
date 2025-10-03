package com.schoolmanagementsystem.SchoolManagementSystem.controller;

import com.schoolmanagementsystem.SchoolManagementSystem.entity.TeacherAssignment;
import com.schoolmanagementsystem.SchoolManagementSystem.service.TeacherAssignmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sections/{sectionId}/class-teachers")
public class TeacherAssignmentController {


    private final TeacherAssignmentService assignmentService;

    public TeacherAssignmentController(TeacherAssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping
    public ResponseEntity<TeacherAssignment> assignClassTeacher(
            @PathVariable Long sectionId,
            @RequestBody TeacherAssignment assignment) {
        TeacherAssignment created = assignmentService.assignClassTeacher(sectionId, assignment);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<TeacherAssignment>> getClassTeachersBySection(@PathVariable Long sectionId) {
        return ResponseEntity.ok(assignmentService.getClassTeachersBySection(sectionId));
    }

    @DeleteMapping("/{assignmentId}")
    public ResponseEntity<Void> removeClassTeacher(@PathVariable Long assignmentId) {
        assignmentService.removeClassTeacher(assignmentId);
        return ResponseEntity.ok().build();
    }




}
