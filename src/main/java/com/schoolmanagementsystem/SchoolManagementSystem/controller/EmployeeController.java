package com.schoolmanagementsystem.SchoolManagementSystem.controller;

import com.schoolmanagementsystem.SchoolManagementSystem.entity.Employee;
import com.schoolmanagementsystem.SchoolManagementSystem.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {


    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Create Employee

    @PostMapping
    public ResponseEntity<String> createEmployee(@Valid @RequestBody Employee employee) {
        employeeService.saveEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body("Employee created successfully");
    }

    // Get all employees

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees(
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size) {

        List<Employee> employees = (cursor == null)
                ? employeeService.getAllEmployees()
                : employeeService.getEmployeesAfterCursor(cursor, size);

        return ResponseEntity.ok(employees);
    }

    // Get employee by ID

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    // Update Employee

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@Valid @PathVariable Long id, @RequestBody Employee employee) {
        Employee updatedEmployee = employeeService.updateEmployee(id, employee);
        return ResponseEntity.ok(updatedEmployee);
    }

    // Delete Employee

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployeeById(id);
        return ResponseEntity.ok("Employee deleted successfully");
    }

    @GetMapping("/filterByRole/{roleName}")
    public List<Employee> getEmployeesByRole(@PathVariable String roleName) {
        return employeeService.getEmployeesByRoleName(roleName);
    }


}




