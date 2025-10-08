package com.schoolmanagementsystem.SchoolManagementSystem.service;

import com.schoolmanagementsystem.SchoolManagementSystem.GlobalExceptionHandler.ResourceNotFoundException;
import com.schoolmanagementsystem.SchoolManagementSystem.entity.Employee;
import com.schoolmanagementsystem.SchoolManagementSystem.repository.EmployeeRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeService {


    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: ", "id", id));
    }

    @Transactional
    public Employee updateEmployee(Long id, Employee updatedEmployee) {
        Employee employee = getEmployeeById(id);

        employee.setEmployeeName(updatedEmployee.getEmployeeName());
        employee.setMobileNumber(updatedEmployee.getMobileNumber());
        employee.setMonthlySalary(updatedEmployee.getMonthlySalary());
        employee.setPictureUrl(updatedEmployee.getPictureUrl());
        employee.setDateOfJoining(updatedEmployee.getDateOfJoining());
        employee.setGender(updatedEmployee.getGender());
        employee.setExperience(updatedEmployee.getExperience());
        employee.setNationalId(updatedEmployee.getNationalId());
        employee.setReligion(updatedEmployee.getReligion());
        employee.setEmail(updatedEmployee.getEmail());
        employee.setEducation(updatedEmployee.getEducation());
        employee.setBloodGroup(updatedEmployee.getBloodGroup());
        employee.setDateOfBirth(updatedEmployee.getDateOfBirth());
        employee.setHomeAddress(updatedEmployee.getHomeAddress());
        employee.setSubjectToTeach(updatedEmployee.getSubjectToTeach());
        employee.setRelationshipName(updatedEmployee.getRelationshipName());

        return employeeRepository.save(employee);
    }

    @Transactional
    public void deleteEmployeeById(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employee not found with id: ", "id", id);
        }
        employeeRepository.deleteById(id);
    }

    public List<Employee> getEmployeesAfterCursor(Long cursor, int size) {
        Pageable pageable = PageRequest.of(0, size);
        if (cursor == null) {
            return employeeRepository.findAll(pageable).getContent();
        } else {
            return employeeRepository.findByIdGreaterThanOrderByIdAsc(cursor, pageable);
        }
    }

    public List<Employee> getEmployeesByRoleName(String roleName) {
        return employeeRepository.findByRole_RoleName(roleName);
    }


}

