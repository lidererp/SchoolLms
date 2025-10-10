package com.schoolmanagementsystem.SchoolManagementSystem.controller;

import com.schoolmanagementsystem.SchoolManagementSystem.entity.TimetableConfig;
import com.schoolmanagementsystem.SchoolManagementSystem.service.TimetableConfigService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/timetable-configs")
public class TimetableConfigController {


    private final TimetableConfigService timetableConfigService;

    public TimetableConfigController(TimetableConfigService timetableConfigService) {
        this.timetableConfigService = timetableConfigService;
    }

    @PostMapping
    public ResponseEntity<TimetableConfig> create(@RequestBody TimetableConfig timetableConfig) {
        return ResponseEntity.ok(timetableConfigService.createTimetableConfig(timetableConfig));
    }

    @GetMapping
    public ResponseEntity<List<TimetableConfig>> getAll() {
        return ResponseEntity.ok(timetableConfigService.getAllTimetableConfigs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimetableConfig> get(@PathVariable Long id) {
        return ResponseEntity.ok(timetableConfigService.getTimetableConfig(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TimetableConfig> update(@PathVariable Long id, @RequestBody TimetableConfig timetableConfig) {
        return ResponseEntity.ok(timetableConfigService.updateTimetableConfig(id, timetableConfig));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        timetableConfigService.deleteTimetableConfig(id);
        return ResponseEntity.noContent().build();
    }


}