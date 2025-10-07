package com.schoolmanagementsystem.SchoolManagementSystem.controller;

import com.schoolmanagementsystem.SchoolManagementSystem.dtos.CurriculumRequestDTO;
import com.schoolmanagementsystem.SchoolManagementSystem.dtos.CurriculumResponseDTO;
import com.schoolmanagementsystem.SchoolManagementSystem.service.CurriculumService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/curriculums")
@RequiredArgsConstructor
public class CurriculumController {


    private final CurriculumService curriculumService;

    @PostMapping
    public ResponseEntity<String> createCurriculum(@Valid @RequestBody CurriculumRequestDTO dto) {

        String message = curriculumService.createCurriculum(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(message);

    }


    @GetMapping("/{id}")
    public ResponseEntity<CurriculumResponseDTO> getCurriculum(@PathVariable Long id) {

        return ResponseEntity.ok(curriculumService.getCurriculum(id));

    }

    @GetMapping
    public ResponseEntity<List<CurriculumResponseDTO>> getAllCurriculums() {

        return ResponseEntity.ok(curriculumService.getAllCurriculums());

    }

    @PutMapping("/{id}")
    public ResponseEntity<CurriculumResponseDTO> updateCurriculum(
            @PathVariable Long id,
            @Valid @RequestBody CurriculumRequestDTO dto) {

        return ResponseEntity.ok(curriculumService.updateCurriculum(id, dto));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCurriculum(@PathVariable Long id) {

        curriculumService.deleteCurriculum(id);

        return ResponseEntity.ok("Curriculum deleted successfully");

    }


}
