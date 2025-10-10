package com.schoolmanagementsystem.SchoolManagementSystem.controller;

import com.schoolmanagementsystem.SchoolManagementSystem.entity.BreakSlot;
import com.schoolmanagementsystem.SchoolManagementSystem.service.BreakSlotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/break-slots")
public class BreakSlotController {


    private final BreakSlotService breakSlotService;

    public BreakSlotController(BreakSlotService breakSlotService) {
        this.breakSlotService = breakSlotService;
    }

    @PostMapping
    public ResponseEntity<BreakSlot> create(@RequestBody BreakSlot breakSlot) {
        return ResponseEntity.ok(breakSlotService.createBreakSlot(breakSlot));
    }

    @GetMapping
    public ResponseEntity<List<BreakSlot>> getAll() {
        return ResponseEntity.ok(breakSlotService.getAllBreakSlots());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BreakSlot> get(@PathVariable Long id) {
        return ResponseEntity.ok(breakSlotService.getBreakSlot(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BreakSlot> update(@PathVariable Long id, @RequestBody BreakSlot breakSlot) {
        return ResponseEntity.ok(breakSlotService.updateBreakSlot(id, breakSlot));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        breakSlotService.deleteBreakSlot(id);
        return ResponseEntity.noContent().build();
    }


}
