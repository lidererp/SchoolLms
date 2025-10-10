package com.schoolmanagementsystem.SchoolManagementSystem.controller;

import com.schoolmanagementsystem.SchoolManagementSystem.dtos.AssignSectionRequestDTO;
import com.schoolmanagementsystem.SchoolManagementSystem.dtos.StudentDTO;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.Student;
import com.schoolmanagementsystem.SchoolManagementSystem.enums.StudentStatus;
import com.schoolmanagementsystem.SchoolManagementSystem.service.StudentService;
import com.schoolmanagementsystem.SchoolManagementSystem.utility.ApiResponse;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {


    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createStudent(@Valid @RequestBody Student student) throws MessagingException {

        studentService.createStudent(student);
        return ResponseEntity.status(201).body(ApiResponse.of("Student created successfully ✅", 201));

    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody Student student) {
        studentService.updateStudent(id, student);
        return ResponseEntity.ok(ApiResponse.of("Student updated successfully ✅", 200));
    }


    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @GetMapping
    public List<StudentDTO> getStudentsWithCursorDesc(
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size) {
        return studentService.getStudentsWithCursorDescDTO(cursor, size);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok(ApiResponse.of("Student and academic records deleted successfully ✅", 200));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Student> updateStudentStatus(
            @PathVariable Long id,
            @RequestParam StudentStatus status) {
        Student updatedStudent = studentService.updateStatus(id, status);
        return ResponseEntity.ok(updatedStudent);
    }


    @PatchMapping("/status/alumni/standard/{standardId}")
    public ResponseEntity<String> markStudentsAlumniByStandard(@PathVariable Long standardId) {
        int updatedCount = studentService.markStudentsAlumniByStandard(standardId);
        return ResponseEntity.ok(updatedCount + " students marked as ALUMNI in standard ID: " + standardId);
    }

    @GetMapping("/search")
    public ResponseEntity<List<StudentDTO>> getStudentsByStandardAndAcademicYear(
            @RequestParam Long standardId,
            @RequestParam String academicYear,
            @RequestParam(required = false) Long sectionId // optional
    ) {
        List<StudentDTO> students = studentService.getStudentsByStandardAndAcademicYear(standardId, academicYear, sectionId);
        return ResponseEntity.ok(students);
    }

    @PatchMapping("/assign-section")
    public ResponseEntity<String> patchAssignSection(@RequestBody AssignSectionRequestDTO request) {
        String message = studentService.assignSectionToStudents(request);
        return ResponseEntity.ok(message);
    }

    @PutMapping("{id}/promote")
    public ResponseEntity<StudentDTO> promoteStudent(
            @PathVariable Long id,
            @RequestParam Long newStandardId,
            @RequestParam(required = false) Long newSectionId) {

        Student student = studentService.promoteStudent(id, newStandardId, newSectionId);
        return ResponseEntity.ok(studentService.toDTO(student));
    }


}

