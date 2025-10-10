package com.schoolmanagementsystem.SchoolManagementSystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeLeave {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotNull(message = "Employee ID is required")
    public Long employeeId;

    @NotNull(message = "Date is required")
    public LocalDate date;

    @NotNull(message = "onLeave flag is required")
    public Boolean onLeave;

    private LocalDateTime loginDateTime;

    private LocalDateTime logoutDateTime;


}
