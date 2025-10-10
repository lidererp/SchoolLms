package com.schoolmanagementsystem.SchoolManagementSystem.repository;


import com.schoolmanagementsystem.SchoolManagementSystem.entity.EmployeeLeave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeLeaveRepository extends JpaRepository<EmployeeLeave, Long> {


    Optional<EmployeeLeave> findByEmployeeIdAndDate(Long employeeId, LocalDate date);

    // For cursor-based pagination
    @Query("SELECT e FROM EmployeeLeave e WHERE e.id > :cursor ORDER BY e.id ASC")
    List<EmployeeLeave> findByIdAfter(@Param("cursor") Long cursor, org.springframework.data.domain.Pageable pageable);


}
