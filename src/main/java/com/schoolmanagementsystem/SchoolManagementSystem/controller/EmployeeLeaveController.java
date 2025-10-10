package com.schoolmanagementsystem.SchoolManagementSystem.controller;

import com.schoolmanagementsystem.SchoolManagementSystem.entity.EmployeeLeave;
import com.schoolmanagementsystem.SchoolManagementSystem.service.EmployeeLeaveService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leaves")
public class EmployeeLeaveController {


    private final EmployeeLeaveService leaveService;

    public EmployeeLeaveController(EmployeeLeaveService leaveService) {
        this.leaveService = leaveService;
    }

    // Create leave
    @PostMapping
    public ResponseEntity<EmployeeLeave> createLeave(@RequestBody EmployeeLeave leave) {
        EmployeeLeave savedLeave = leaveService.createLeave(leave);
        return ResponseEntity.ok(savedLeave);
    }

    @GetMapping("/paginated")
    public ResponseEntity<List<EmployeeLeave>> getLeavesPaginated(
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size) {

        List<EmployeeLeave> leaves = leaveService.getLeavesWithPagination(cursor, size);
        return ResponseEntity.ok(leaves);
    }

    // Get leave by ID
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeLeave> getLeaveById(@PathVariable Long id) {
        EmployeeLeave leave = leaveService.getLeaveById(id);
        return ResponseEntity.ok(leave);
    }

    // Update leave
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeLeave> updateLeave(@PathVariable Long id, @RequestBody EmployeeLeave leave) {
        EmployeeLeave updatedLeave = leaveService.updateLeave(id, leave);
        return ResponseEntity.ok(updatedLeave);
    }

    // Delete leave
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLeave(@PathVariable Long id) {
        leaveService.deleteLeave(id);
        return ResponseEntity.ok("Leave record deleted successfully.");
    }


}
