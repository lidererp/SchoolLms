package com.schoolmanagementsystem.SchoolManagementSystem.repository;


import com.schoolmanagementsystem.SchoolManagementSystem.entity.Employee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {


    List<Employee> findByIdGreaterThanOrderByIdAsc(Long id, Pageable pageable);

    List<Employee> findByRole_RoleName(String roleName);


}

