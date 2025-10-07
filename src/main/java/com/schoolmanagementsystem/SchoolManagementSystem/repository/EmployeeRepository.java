package com.schoolmanagementsystem.SchoolManagementSystem.repository;


import com.schoolmanagementsystem.SchoolManagementSystem.entity.Employee;
import com.schoolmanagementsystem.SchoolManagementSystem.enums.EmployeeRole;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {


    // For role-based fetching
    List<Employee> findByEmployeeRole(EmployeeRole role);

    // For cursor-based pagination
    List<Employee> findByIdGreaterThanOrderByIdAsc(Long id, Pageable pageable);



}

