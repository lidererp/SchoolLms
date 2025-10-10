package com.schoolmanagementsystem.SchoolManagementSystem.controller;

import com.schoolmanagementsystem.SchoolManagementSystem.dtos.TimetableGenerationRequest;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.Timetable;
import com.schoolmanagementsystem.SchoolManagementSystem.service.TimetableGenerationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/timetables")
public class TimetableGenerationController {


    private final TimetableGenerationService timetableGenerationService;

    public TimetableGenerationController(TimetableGenerationService timetableGenerationService) {
        this.timetableGenerationService = timetableGenerationService;
    }

    @PostMapping("/generate")
    public ResponseEntity<Timetable> generateTimetable(
            @RequestBody TimetableGenerationRequest request) {
        return ResponseEntity.ok(
                timetableGenerationService.generateTimetable(
                        request.getConfigId(),
                        request.getSubjectAllocationMapping()
                )
        );
    }

    @PutMapping("/{timetableId}")
    public ResponseEntity<Timetable> editTimetable(
            @PathVariable Long timetableId,
            @RequestBody TimetableGenerationRequest request) {
        return ResponseEntity.ok(
                timetableGenerationService.editTimetable(
                        timetableId,
                        request.getConfigId(),
                        request.getSubjectAllocationMapping()
                )
        );
    }

    @GetMapping("/all")
    public ResponseEntity<List<Timetable>> getAllTimetables() {
        return ResponseEntity.ok(timetableGenerationService.getAllTimetables());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Timetable> getTimetableById(@PathVariable Long id) {
        Timetable timetable = timetableGenerationService.getTimetableById(id);
        return ResponseEntity.ok(timetable);
    }

}
